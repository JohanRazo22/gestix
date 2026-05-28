const API = 'http://localhost:8080/api';
let taskToDelete = null;
let projectToDelete = null;

const token = localStorage.getItem('token');
const username = localStorage.getItem('username');

if (!token || token === 'null' || token === 'undefined') {
    window.location.href = 'login.html';
}

document.getElementById('navUsername').textContent = username || '';

const statusLabel = { PENDING: 'Pendiente', IN_PROGRESS: 'En progreso', COMPLETED: 'Completada' };
const priorityLabel = { HIGH: 'Alta', MEDIUM: 'Media', LOW: 'Baja' };

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
    const filterStatus   = window._activeStatus   || '';
    const filterPriority = window._activePriority || '';
    const searchTerm     = window._searchTerm     || '';
    const filterCategory = window._activeCategory || '';
    const filterOverdue  = window._activeOverdue  || false;

    let res;
    try {
        res = await fetch(`${API}/tasks`, { headers: authHeaders() });
    } catch (networkErr) {
        console.error('Error de red:', networkErr);
        showError('No se pudo conectar al servidor. Verifica que esté en ejecución.');
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
    if (window._onTasksLoaded) {
        try { window._onTasksLoaded(allTasks); } catch(e) { console.error('_onTasksLoaded error:', e); }
    }

    let tasks = [...allTasks];
    if (filterCategory) tasks = tasks.filter(t =>
        filterCategory === 'Sin proyecto' ? !t.category : t.category === filterCategory
    );
    if (filterStatus)   tasks = tasks.filter(t => t.status   === filterStatus);
    if (filterPriority) tasks = tasks.filter(t => t.priority === filterPriority);
    if (filterOverdue) {
        const today = new Date(); today.setHours(0,0,0,0);
        tasks = tasks.filter(t =>
            t.dueDate && t.status !== 'COMPLETED' &&
            new Date(t.dueDate + 'T00:00:00') < today
        );
    }
    if (searchTerm)     tasks = tasks.filter(t =>
        t.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        (t.description && t.description.toLowerCase().includes(searchTerm.toLowerCase()))
    );

    const list  = document.getElementById('taskList');
    const empty = document.getElementById('emptyState');

    if (tasks.length === 0) {
        list.innerHTML = '';
        const iconEl    = document.getElementById('emptyIcon');
        const titleEl   = document.getElementById('emptyTitle');
        const subEl     = document.getElementById('emptySubtitle');
        const actionEl  = document.getElementById('emptyAction');
        const statusLabels = { PENDING: 'pendientes', IN_PROGRESS: 'en progreso', COMPLETED: 'completadas' };
        const priorityLabels = { HIGH: 'alta', MEDIUM: 'media', LOW: 'baja' };
        if (iconEl && titleEl && subEl && actionEl) {
            if (searchTerm) {
                iconEl.textContent   = '🔍';
                titleEl.textContent  = 'Sin resultados';
                subEl.textContent    = `No se encontraron tareas para "${searchTerm}"`;
                actionEl.classList.add('hidden');
            } else if (filterOverdue) {
                iconEl.textContent   = '🎉';
                titleEl.textContent  = 'Ninguna tarea vencida';
                subEl.textContent    = 'Vas al día con todas tus fechas límite';
                actionEl.classList.add('hidden');
            } else if (filterStatus) {
                const lbl = statusLabels[filterStatus] || filterStatus;
                iconEl.textContent   = filterStatus === 'COMPLETED' ? '✅' : filterStatus === 'IN_PROGRESS' ? '⏳' : '📌';
                titleEl.textContent  = `No hay tareas ${lbl}`;
                subEl.textContent    = 'Cambia el filtro o crea una nueva tarea';
                actionEl.classList.add('hidden');
            } else if (filterPriority) {
                const lbl = priorityLabels[filterPriority] || filterPriority;
                iconEl.textContent   = '🎯';
                titleEl.textContent  = `No hay tareas de prioridad ${lbl}`;
                subEl.textContent    = 'Prueba con otro filtro de prioridad';
                actionEl.classList.add('hidden');
            } else if (filterCategory) {
                const name = filterCategory === 'Sin proyecto' ? 'Sin proyecto' : `"${filterCategory}"`;
                iconEl.textContent   = '📁';
                titleEl.textContent  = `No hay tareas en ${name}`;
                subEl.textContent    = 'Crea una tarea y asígnale este proyecto';
                actionEl.classList.remove('hidden');
            } else {
                iconEl.textContent   = '📋';
                titleEl.textContent  = 'No hay tareas aquí';
                subEl.textContent    = 'Crea tu primera tarea para empezar';
                actionEl.classList.remove('hidden');
            }
        }
        empty.classList.remove('hidden');
        return;
    }

    empty.classList.add('hidden');
    const render = window._renderTask || (t => `<div class="p-3">${t.title}</div>`);
    list.innerHTML = tasks.map(render).join('');
    const countEl = document.getElementById('taskCount');
    if (countEl) countEl.textContent = `${tasks.length} tarea${tasks.length !== 1 ? 's' : ''}`;
}

function showError(msg) {
    const empty = document.getElementById('emptyState');
    if (empty) {
        empty.innerHTML = `
            <div class="text-4xl mb-4">⚠️</div>
            <p class="font-bold text-red-500 text-lg mb-2">Error al cargar tareas</p>
            <p class="text-gray-400 mb-6">${msg}</p>
            <button onclick="window.location.href='login.html'"
                class="font-bold px-6 py-3 rounded-xl text-white text-sm bg-indigo-600">
                Volver a iniciar sesión
            </button>`;
        empty.classList.remove('hidden');
    }
}

async function toggleTaskComplete(id, event) {
    if (event) event.stopPropagation();

    const task = (window._allTasks || []).find(t => t.id === id);
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
        if (!res.ok) throw new Error('No se pudo actualizar la tarea');

        await loadTasks();
        if (typeof window._renderProjectDetail === 'function' && window._currentProject) {
            window._renderProjectDetail();
        }
    } catch (err) {
        alert(err.message || 'Error al actualizar la tarea');
    }
}
window.toggleTaskComplete = toggleTaskComplete;

function openModal() {
    document.getElementById('modalTitle').textContent = 'Nueva Tarea';
    document.getElementById('taskForm').reset();
    document.getElementById('taskId').value = '';
    document.getElementById('taskError').classList.add('hidden');
    document.getElementById('taskModal').classList.remove('hidden');
}

async function openEditModal(id) {
    const res = await fetch(`${API}/tasks/${id}`, { headers: authHeaders() });
    const task = await res.json();

    document.getElementById('modalTitle').textContent = 'Editar Tarea';
    document.getElementById('taskId').value = task.id;
    document.getElementById('taskTitle').value = task.title;
    document.getElementById('taskDescription').value = task.description || '';
    document.getElementById('taskStatus').value = task.status;
    document.getElementById('taskPriority').value = task.priority;
    document.getElementById('taskCategory').value = task.category || '';
    document.getElementById('taskDueDate').value = task.dueDate || '';
    document.getElementById('taskError').classList.add('hidden');
    document.getElementById('taskModal').classList.remove('hidden');
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

        if (!res.ok) throw new Error('Error al guardar la tarea');

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
    if (name === 'Sin proyecto') {
        return all.filter(t => !t.category || !String(t.category).trim());
    }
    return all.filter(t => t.category === name);
}

function openDeleteProjectModal(name, id = null) {
    projectToDelete = { name, id };
    const count = tasksInProject(name).length;
    const titleEl = document.getElementById('deleteProjectTitle');
    const msgEl   = document.getElementById('deleteProjectMessage');

    if (titleEl) titleEl.textContent = `¿Eliminar "${name}"?`;

    if (msgEl) {
        if (count === 0) {
            msgEl.textContent = 'Este proyecto no tiene tareas. Se eliminará de tu lista de proyectos.';
        } else if (count === 1) {
            msgEl.textContent = 'Si eliminas este proyecto, se borrará permanentemente 1 tarea que contiene.';
        } else {
            msgEl.textContent = `Si eliminas este proyecto, se borrarán permanentemente las ${count} tareas que contiene.`;
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
        btn.textContent = 'Eliminar proyecto';
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
        btn.textContent = 'Eliminando...';
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
            if (!res.ok) throw new Error('No se pudo eliminar el proyecto');
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
                if (!res.ok) throw new Error('No se pudo eliminar una tarea del proyecto');
            }
        }

        closeDeleteProjectModal();

        if (typeof showProjectsList === 'function') showProjectsList();
        await loadProjects();
        await loadTasks();
        if (typeof renderProjects === 'function') renderProjects();
    } catch (err) {
        alert(err.message || 'Error al eliminar el proyecto');
        if (btn) {
            btn.disabled = false;
            btn.textContent = 'Eliminar proyecto';
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
