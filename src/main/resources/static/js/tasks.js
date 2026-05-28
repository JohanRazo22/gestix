const API = 'http://localhost:8080/api';
let taskToDelete = null;
let projectToDelete = null;

const token = localStorage.getItem('token');
const username = localStorage.getItem('username');

if (!token || token === 'null' || token === 'undefined') {
    window.location.href = 'login.html';
}

document.getElementById('navUsername').textContent = username || '';

function authHeaders() {
    return { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };
}

async function migrateLocalProjects() {
    const local = JSON.parse(localStorage.getItem('gestix_projects') || '[]');
    if (!local.length) return;

    for (const name of local) {
        if (!name || !String(name).trim()) continue;
        try {
            await fetch(`${API}/projects`, {
                method: 'POST',
                headers: authHeaders(),
                body: JSON.stringify({ name: String(name).trim() })
            });
        } catch (_) { /* ignorar duplicados o errores de red en migración */ }
    }
    localStorage.removeItem('gestix_projects');
}

async function loadProjects() {
    try {
        const res = await fetch(`${API}/projects`, { headers: authHeaders() });
        if (res.status === 401 || res.status === 403) {
            logout();
            return;
        }
        if (!res.ok) {
            window._allProjects = [];
            return;
        }
        window._allProjects = await res.json();
    } catch (err) {
        console.error('Error cargando proyectos:', err);
        window._allProjects = [];
    }
}

async function loadTasks() {
    let res;
    try {
        res = await fetch(`${API}/tasks`, { headers: authHeaders() });
    } catch (networkErr) {
        console.error('Error de red:', networkErr);
        showError(t('errors.network'));
        return;
    }

    if (res.status === 401 || res.status === 403) {
        logout();
        return;
    }

    let allTasks;
    try {
        allTasks = await res.json();
    } catch (parseErr) {
        console.error('Respuesta no es JSON. Status:', res.status, 'URL:', res.url);
        logout();
        return;
    }

    window._allTasks = allTasks;
    renderTaskListFromCache(allTasks);
    if (typeof refreshLocaleUI === 'function' && window._i18nBootstrapDone) {
        refreshLocaleUI();
    }
}

function renderTaskListFromCache(allTasks) {
    if (window._onTasksLoaded) {
        try { window._onTasksLoaded(allTasks); } catch(e) { console.error('_onTasksLoaded error:', e); }
    }

    const filterStatus   = window._activeStatus   || '';
    const filterPriority = window._activePriority || '';
    const searchTerm     = window._searchTerm     || '';
    const filterCategory = window._activeCategory || '';
    const filterOverdue  = window._activeOverdue  || false;

    let tasks = [...allTasks];
    if (filterCategory) {
        tasks = tasks.filter(task =>
            isNoProjectName(filterCategory) ? !task.category : task.category === filterCategory
        );
    }
    if (filterStatus)   tasks = tasks.filter(task => task.status   === filterStatus);
    if (filterPriority) tasks = tasks.filter(task => task.priority === filterPriority);
    if (filterOverdue) {
        const today = new Date(); today.setHours(0,0,0,0);
        tasks = tasks.filter(task =>
            task.dueDate && task.status !== 'COMPLETED' &&
            new Date(task.dueDate + 'T00:00:00') < today
        );
    }
    if (searchTerm)     tasks = tasks.filter(task =>
        task.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        (task.description && task.description.toLowerCase().includes(searchTerm.toLowerCase()))
    );

    const list  = document.getElementById('taskList');
    const empty = document.getElementById('emptyState');

    if (tasks.length === 0) {
        list.innerHTML = '';
        const iconEl    = document.getElementById('emptyIcon');
        const titleEl   = document.getElementById('emptyTitle');
        const subEl     = document.getElementById('emptySubtitle');
        const actionEl  = document.getElementById('emptyAction');
        if (iconEl && titleEl && subEl && actionEl) {
            if (searchTerm) {
                iconEl.textContent   = '🔍';
                titleEl.textContent  = t('tasks.empty.noResults');
                subEl.textContent    = t('tasks.empty.noResultsFor', { q: searchTerm });
                actionEl.classList.add('hidden');
            } else if (filterOverdue) {
                iconEl.textContent   = '🎉';
                titleEl.textContent  = t('tasks.empty.noOverdue');
                subEl.textContent    = t('tasks.empty.noOverdueSub');
                actionEl.classList.add('hidden');
            } else if (filterStatus) {
                const lbl = tStatus(filterStatus, true);
                iconEl.textContent   = filterStatus === 'COMPLETED' ? '✅' : filterStatus === 'IN_PROGRESS' ? '⏳' : '📌';
                titleEl.textContent  = t('tasks.empty.noStatus', { status: lbl });
                subEl.textContent    = t('tasks.empty.changeFilter');
                actionEl.classList.add('hidden');
            } else if (filterPriority) {
                const lbl = tPriority(filterPriority, true);
                iconEl.textContent   = '🎯';
                titleEl.textContent  = t('tasks.empty.noPriority', { priority: lbl });
                subEl.textContent    = t('tasks.empty.tryOtherPriority');
                actionEl.classList.add('hidden');
            } else if (filterCategory) {
                const name = isNoProjectName(filterCategory) ? noProjectLabel() : `"${filterCategory}"`;
                iconEl.textContent   = '📁';
                titleEl.textContent  = t('tasks.empty.noInProject', { name });
                subEl.textContent    = t('tasks.empty.assignProject');
                actionEl.classList.remove('hidden');
            } else {
                iconEl.textContent   = '📋';
                titleEl.textContent  = t('tasks.empty.title');
                subEl.textContent    = t('tasks.empty.subtitle');
                actionEl.classList.remove('hidden');
            }
        }
        empty.classList.remove('hidden');
        return;
    }

    empty.classList.add('hidden');
    const render = window._renderTask || (task => `<div class="p-3">${task.title}</div>`);
    list.innerHTML = tasks.map(render).join('');
    const countEl = document.getElementById('taskCount');
    if (countEl) countEl.textContent = formatTaskCount(tasks.length);
}

function reRenderTasksUI() {
    if (!window._allTasks) return;
    renderTaskListFromCache(window._allTasks);
}
window.reRenderTasksUI = reRenderTasksUI;

function showError(msg) {
    const empty = document.getElementById('emptyState');
    if (empty) {
        empty.innerHTML = `
            <div class="text-4xl mb-4">⚠️</div>
            <p class="font-bold text-red-500 text-lg mb-2">${t('errors.loadTasks')}</p>
            <p class="text-gray-400 mb-6">${msg}</p>
            <button onclick="window.location.href='login.html'"
                class="font-bold px-6 py-3 rounded-xl text-white text-sm bg-indigo-600">
                ${t('errors.backToLogin')}
            </button>`;
        empty.classList.remove('hidden');
    }
}

async function toggleTaskComplete(id, event) {
    if (event) event.stopPropagation();

    const task = (window._allTasks || []).find(item => item.id === id);
    if (!task) return;

    const newStatus = task.status === 'COMPLETED' ? 'PENDING' : 'COMPLETED';
    const body = {
        title: task.title,
        description: task.description || '',
        status: newStatus,
        priority: task.priority,
        category: task.category || null,
        dueDate: task.dueDate || null
    };

    try {
        const res = await fetch(`${API}/tasks/${id}`, {
            method: 'PUT',
            headers: authHeaders(),
            body: JSON.stringify(body)
        });
        if (res.status === 401 || res.status === 403) {
            logout();
            return;
        }
        if (!res.ok) throw new Error(t('errors.updateTask'));

        await loadTasks();
        if (typeof window._renderProjectDetail === 'function' && window._currentProject) {
            window._renderProjectDetail();
        }
    } catch (err) {
        alert(err.message || t('errors.updateTask'));
    }
}
window.toggleTaskComplete = toggleTaskComplete;

function openModal() {
    document.getElementById('modalTitle').textContent = t('modal.task.new');
    document.getElementById('taskForm').reset();
    document.getElementById('taskId').value = '';
    document.getElementById('taskError').classList.add('hidden');
    document.getElementById('taskModal').classList.remove('hidden');
    if (typeof refreshTaskFormSelects === 'function') refreshTaskFormSelects();
}

async function openEditModal(id) {
    const res = await fetch(`${API}/tasks/${id}`, { headers: authHeaders() });
    const task = await res.json();

    document.getElementById('modalTitle').textContent = t('modal.task.edit');
    document.getElementById('taskId').value = task.id;
    document.getElementById('taskTitle').value = task.title;
    document.getElementById('taskDescription').value = task.description || '';
    document.getElementById('taskStatus').value = task.status;
    document.getElementById('taskPriority').value = task.priority;
    document.getElementById('taskCategory').value = task.category || '';
    document.getElementById('taskDueDate').value = task.dueDate || '';
    document.getElementById('taskError').classList.add('hidden');
    document.getElementById('taskModal').classList.remove('hidden');
    if (typeof refreshTaskFormSelects === 'function') refreshTaskFormSelects();
}

function closeModal() {
    document.getElementById('taskModal').classList.add('hidden');
}

async function handleSaveTask(e) {
    e.preventDefault();
    const errorEl = document.getElementById('taskError');
    errorEl.classList.add('hidden');

    const id = document.getElementById('taskId').value;
    const body = {
        title: document.getElementById('taskTitle').value,
        description: document.getElementById('taskDescription').value,
        status: document.getElementById('taskStatus').value,
        priority: document.getElementById('taskPriority').value,
        category: document.getElementById('taskCategory').value || null,
        dueDate: document.getElementById('taskDueDate').value || null
    };

    try {
        const res = await fetch(`${API}/tasks${id ? '/' + id : ''}`, {
            method: id ? 'PUT' : 'POST',
            headers: authHeaders(),
            body: JSON.stringify(body)
        });

        if (!res.ok) throw new Error(t('errors.saveTask'));

        closeModal();
        loadTasks();
    } catch (err) {
        errorEl.textContent = err.message;
        errorEl.classList.remove('hidden');
    }
}

function openDeleteModal(id) {
    taskToDelete = id;
    document.getElementById('deleteModal').classList.remove('hidden');
}

function closeDeleteModal() {
    taskToDelete = null;
    document.getElementById('deleteModal').classList.add('hidden');
}

async function confirmDelete() {
    if (!taskToDelete) return;
    await fetch(`${API}/tasks/${taskToDelete}`, { method: 'DELETE', headers: authHeaders() });
    closeDeleteModal();
    loadTasks();
}

function tasksInProject(name) {
    const all = window._allTasks || [];
    if (isNoProjectName(name)) {
        return all.filter(task => !task.category || !String(task.category).trim());
    }
    return all.filter(task => task.category === name);
}

function openDeleteProjectModal(name, id = null) {
    projectToDelete = { name, id };
    const count = tasksInProject(name).length;
    const titleEl = document.getElementById('deleteProjectTitle');
    const msgEl   = document.getElementById('deleteProjectMessage');
    const displayName = isNoProjectName(name) ? noProjectLabel() : name;

    if (titleEl) titleEl.textContent = t('modal.delete.projectConfirm', { name: displayName });

    if (msgEl) {
        if (count === 0) {
            msgEl.textContent = t('modal.delete.projectEmpty');
        } else if (count === 1) {
            msgEl.textContent = t('modal.delete.projectOne');
        } else {
            msgEl.textContent = t('modal.delete.projectMany', { n: count });
        }
    }

    document.getElementById('deleteProjectModal').classList.remove('hidden');
}
window.openDeleteProjectModal = openDeleteProjectModal;

function closeDeleteProjectModal() {
    projectToDelete = null;
    const btn = document.getElementById('deleteProjectConfirmBtn');
    if (btn) {
        btn.disabled = false;
        btn.textContent = t('modal.delete.projectBtn');
    }
    document.getElementById('deleteProjectModal').classList.add('hidden');
}

async function confirmDeleteProject() {
    if (!projectToDelete) return;

    const { name, id } = projectToDelete;
    const tasks = tasksInProject(name);
    const btn   = document.getElementById('deleteProjectConfirmBtn');

    if (btn) {
        btn.disabled = true;
        btn.textContent = t('modal.delete.deleting');
    }

    try {
        if (id) {
            const res = await fetch(`${API}/projects/${id}`, {
                method: 'DELETE',
                headers: authHeaders()
            });
            if (res.status === 401 || res.status === 403) {
                logout();
                return;
            }
            if (!res.ok) throw new Error(t('errors.deleteProject'));
        } else {
            for (const task of tasks) {
                const res = await fetch(`${API}/tasks/${task.id}`, {
                    method: 'DELETE',
                    headers: authHeaders()
                });
                if (res.status === 401 || res.status === 403) {
                    logout();
                    return;
                }
                if (!res.ok) throw new Error(t('errors.deleteProjectTask'));
            }
        }

        closeDeleteProjectModal();

        if (typeof showProjectsList === 'function') showProjectsList();
        await loadProjects();
        await loadTasks();
        if (typeof renderProjects === 'function') renderProjects();
    } catch (err) {
        alert(err.message || t('errors.deleteProject'));
        if (btn) {
            btn.disabled = false;
            btn.textContent = t('modal.delete.projectBtn');
        }
    }
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    window.location.href = 'index.html';
}

async function initApp() {
    await migrateLocalProjects();
    await loadProjects();
    await loadTasks();
}

initApp();
