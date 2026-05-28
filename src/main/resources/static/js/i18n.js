const TRANSLATIONS = {
    es: {
        'nav.menu': 'Menú',
        'nav.tasks': 'Mis Tareas',
        'nav.projects': 'Proyectos',
        'nav.calendar': 'Calendario',
        'nav.priorities': 'Prioridades',
        'nav.reports': 'Reportes',
        'nav.settings': 'Configuración',
        'nav.logout': 'Cerrar sesión',
        'header.tasks': 'Mis Tareas',
        'header.projects': 'Proyectos',
        'header.calendar': 'Calendario',
        'header.reports': 'Reportes',
        'header.settings': 'Configuración',
        'tasks.greeting': '¡Hola, {name}! 👋',
        'tasks.subtitle': 'Aquí tienes el resumen de tus tareas y progreso.',
        'tasks.newTask': 'Nueva Tarea',
        'tasks.myTasks': 'Mis Tareas',
        'tasks.search': 'Buscar tareas...',
        'tasks.filter.all': 'Todas',
        'tasks.filter.pending': 'Pendientes',
        'tasks.filter.progress': 'En progreso',
        'tasks.filter.completed': 'Completadas',
        'tasks.filter.overdue': 'Vencidas',
        'tasks.viewAll': 'Ver todas las tareas →',
        'tasks.stat.total': 'Total Tareas',
        'tasks.stat.pending': 'Pendientes',
        'tasks.stat.progress': 'En Progreso',
        'tasks.stat.completed': 'Completadas',
        'tasks.stat.overdue': 'Vencidas',
        'tasks.stat.loading': 'Cargando...',
        'tasks.stat.focus': 'Enfócate 💪',
        'tasks.chart.weekly': 'Progreso semanal',
        'tasks.chart.thisWeek': 'Esta semana',
        'tasks.chart.completed': 'Completado',
        'tasks.chart.completedLabel': 'Completadas',
        'tasks.chart.inProgress': 'En progreso',
        'tasks.chart.pending': 'Pendientes',
        'tasks.chart.overdue': 'Vencidas',
        'tasks.chart.byPriority': 'Por prioridad',
        'tasks.upcoming': 'Próximas tareas',
        'tasks.upcoming.viewCal': 'Ver calendario →',
        'tasks.upcoming.empty': 'No hay tareas próximas 🎉',
        'tasks.empty.create': '+ Crear tarea',
        'tasks.checkbox.complete': 'Marcar como completada',
        'tasks.checkbox.uncomplete': 'Marcar como pendiente',
        'settings.title': 'Configuración ⚙️',
        'settings.subtitle': 'Personaliza tu perfil y preferencias de la aplicación.',
        'settings.profile.title': 'Perfil',
        'settings.profile.subtitle': 'Tu nombre visible en Gestix',
        'settings.profile.username': 'Nombre de usuario',
        'settings.profile.email': 'Email',
        'settings.profile.save': 'Guardar cambios',
        'settings.profile.saved': 'Perfil actualizado correctamente',
        'settings.profile.error': 'No se pudo actualizar el perfil',
        'settings.appearance.title': 'Apariencia',
        'settings.appearance.subtitle': 'Elige cómo se ve Gestix',
        'settings.appearance.theme': 'Tema',
        'settings.appearance.light': 'Claro',
        'settings.appearance.dark': 'Oscuro',
        'settings.appearance.auto': 'Automático',
        'settings.appearance.autoHint': 'Sigue la preferencia de tu sistema',
        'settings.language.title': 'Idioma',
        'settings.language.subtitle': 'Idioma de la interfaz',
        'settings.language.es': 'Español',
        'settings.language.en': 'English',
        'settings.about.title': 'Sobre Gestix',
        'settings.about.subtitle': 'Información, ayuda y legal',
        'settings.about.version': 'Versión',
        'settings.about.tagline': 'Gestor de tareas personal — organiza proyectos, fechas y prioridades en un solo lugar.',
        'settings.about.faq': 'Ayuda / FAQ',
        'settings.about.feedback': 'Enviar feedback',
        'settings.about.terms': 'Términos de uso',
        'settings.about.privacy': 'Política de privacidad',
        'faq.title': 'Preguntas frecuentes',
        'faq.q1': '¿Cómo creo una tarea?',
        'faq.a1': 'Ve a Mis Tareas y pulsa "+ Nueva Tarea", o haz clic en un día del calendario para crear una tarea con fecha predefinida.',
        'faq.q2': '¿Cómo marco una tarea como completada?',
        'faq.a2': 'Haz clic en el círculo a la izquierda de la tarea. Un segundo clic la devuelve a pendiente.',
        'faq.q3': '¿Dónde se guardan mis proyectos?',
        'faq.a3': 'Los proyectos se guardan en tu cuenta en el servidor. Las tareas se vinculan por nombre de proyecto/categoría.',
        'faq.q4': '¿Puedo iniciar sesión con Google o Microsoft?',
        'faq.a4': 'Sí. En la pantalla de login puedes usar tu cuenta de Google o Microsoft además del registro con email.',
        'faq.q5': '¿Cómo cambio el idioma o el tema?',
        'faq.a5': 'Abre Configuración en el menú lateral. Ahí puedes elegir español/inglés y tema claro, oscuro o automático.',
        'feedback.title': 'Enviar feedback',
        'feedback.subtitle': 'Cuéntanos qué mejorarías o si encontraste un problema.',
        'feedback.type': 'Tipo',
        'feedback.type.bug': 'Reportar un error',
        'feedback.type.idea': 'Sugerencia de mejora',
        'feedback.type.other': 'Otro',
        'feedback.message': 'Mensaje',
        'feedback.placeholder': 'Describe tu experiencia, idea o error con el mayor detalle posible...',
        'feedback.send': 'Enviar feedback',
        'feedback.cancel': 'Cancelar',
        'feedback.success': '¡Gracias! Se abrirá tu cliente de correo para enviar el mensaje.',
        'feedback.error': 'Escribe un mensaje antes de enviar.',
        'terms.title': 'Términos de uso',
        'terms.p1': 'Al usar Gestix aceptas utilizar la aplicación de forma responsable y conforme a la ley aplicable.',
        'terms.p2': 'Gestix es una herramienta de productividad personal. Eres responsable del contenido de tus tareas y proyectos.',
        'terms.p3': 'Nos reservamos el derecho de actualizar estas condiciones. El uso continuado del servicio implica su aceptación.',
        'terms.p4': 'El servicio se ofrece "tal cual", sin garantías implícitas de disponibilidad ininterrumpida.',
        'privacy.title': 'Política de privacidad',
        'privacy.p1': 'Recopilamos únicamente la información necesaria para tu cuenta: nombre de usuario, email y las tareas/proyectos que creas.',
        'privacy.p2': 'Tus datos se almacenan de forma segura y no se venden a terceros.',
        'privacy.p3': 'Si inicias sesión con Google o Microsoft, usamos tu email para identificar tu cuenta según las políticas de esos proveedores.',
        'privacy.p4': 'Puedes solicitar la eliminación de tu cuenta y datos contactándonos mediante el formulario de feedback.',
        'legal.close': 'Cerrar',
        'priority.medium': 'Media',
        'priority.low': 'Baja',
        'status.pending': 'Pendiente',
        'status.progress': 'En progreso',
        'status.completed': 'Completada',
        'status.overdue': 'Vencida',
        'chart.priority.high': 'Alta',
        'chart.priority.medium': 'Media',
        'chart.priority.low': 'Baja',
        'user.default': 'Usuario'
    },
    en: {
        'nav.menu': 'Menu',
        'nav.tasks': 'My Tasks',
        'nav.projects': 'Projects',
        'nav.calendar': 'Calendar',
        'nav.priorities': 'Priorities',
        'nav.reports': 'Reports',
        'nav.settings': 'Settings',
        'nav.logout': 'Log out',
        'header.tasks': 'My Tasks',
        'header.projects': 'Projects',
        'header.calendar': 'Calendar',
        'header.reports': 'Reports',
        'header.settings': 'Settings',
        'tasks.greeting': 'Hello, {name}! 👋',
        'tasks.subtitle': 'Here is your task summary and progress.',
        'tasks.newTask': 'New Task',
        'tasks.myTasks': 'My Tasks',
        'tasks.search': 'Search tasks...',
        'tasks.filter.all': 'All',
        'tasks.filter.pending': 'Pending',
        'tasks.filter.progress': 'In progress',
        'tasks.filter.completed': 'Completed',
        'tasks.filter.overdue': 'Overdue',
        'tasks.viewAll': 'View all tasks →',
        'tasks.stat.total': 'Total Tasks',
        'tasks.stat.pending': 'Pending',
        'tasks.stat.progress': 'In Progress',
        'tasks.stat.completed': 'Completed',
        'tasks.stat.overdue': 'Overdue',
        'tasks.stat.loading': 'Loading...',
        'tasks.stat.focus': 'Stay focused 💪',
        'tasks.chart.weekly': 'Weekly progress',
        'tasks.chart.thisWeek': 'This week',
        'tasks.chart.completed': 'Completed',
        'tasks.chart.completedLabel': 'Completed',
        'tasks.chart.inProgress': 'In progress',
        'tasks.chart.pending': 'Pending',
        'tasks.chart.overdue': 'Overdue',
        'tasks.chart.byPriority': 'By priority',
        'tasks.upcoming': 'Upcoming tasks',
        'tasks.upcoming.viewCal': 'View calendar →',
        'tasks.upcoming.empty': 'No upcoming tasks 🎉',
        'tasks.empty.create': '+ Create task',
        'tasks.checkbox.complete': 'Mark as completed',
        'tasks.checkbox.uncomplete': 'Mark as pending',
        'settings.title': 'Settings ⚙️',
        'settings.subtitle': 'Customize your profile and app preferences.',
        'settings.profile.title': 'Profile',
        'settings.profile.subtitle': 'Your display name in Gestix',
        'settings.profile.username': 'Username',
        'settings.profile.email': 'Email',
        'settings.profile.save': 'Save changes',
        'settings.profile.saved': 'Profile updated successfully',
        'settings.profile.error': 'Could not update profile',
        'settings.appearance.title': 'Appearance',
        'settings.appearance.subtitle': 'Choose how Gestix looks',
        'settings.appearance.theme': 'Theme',
        'settings.appearance.light': 'Light',
        'settings.appearance.dark': 'Dark',
        'settings.appearance.auto': 'Auto',
        'settings.appearance.autoHint': 'Follows your system preference',
        'settings.language.title': 'Language',
        'settings.language.subtitle': 'Interface language',
        'settings.language.es': 'Español',
        'settings.language.en': 'English',
        'settings.about.title': 'About Gestix',
        'settings.about.subtitle': 'Information, help and legal',
        'settings.about.version': 'Version',
        'settings.about.tagline': 'Personal task manager — organize projects, dates and priorities in one place.',
        'settings.about.faq': 'Help / FAQ',
        'settings.about.feedback': 'Send feedback',
        'settings.about.terms': 'Terms of use',
        'settings.about.privacy': 'Privacy policy',
        'faq.title': 'Frequently asked questions',
        'faq.q1': 'How do I create a task?',
        'faq.a1': 'Go to My Tasks and click "+ New Task", or click a day on the calendar to create a task with a preset due date.',
        'faq.q2': 'How do I mark a task as completed?',
        'faq.a2': 'Click the circle on the left of the task. Click again to mark it as pending.',
        'faq.q3': 'Where are my projects stored?',
        'faq.a3': 'Projects are saved in your account on the server. Tasks are linked by project/category name.',
        'faq.q4': 'Can I sign in with Google or Microsoft?',
        'faq.a4': 'Yes. On the login screen you can use Google or Microsoft in addition to email registration.',
        'faq.q5': 'How do I change language or theme?',
        'faq.a5': 'Open Settings in the sidebar. There you can choose Spanish/English and light, dark or auto theme.',
        'feedback.title': 'Send feedback',
        'feedback.subtitle': 'Tell us what to improve or if you found an issue.',
        'feedback.type': 'Type',
        'feedback.type.bug': 'Report a bug',
        'feedback.type.idea': 'Feature suggestion',
        'feedback.type.other': 'Other',
        'feedback.message': 'Message',
        'feedback.placeholder': 'Describe your experience, idea or bug in as much detail as possible...',
        'feedback.send': 'Send feedback',
        'feedback.cancel': 'Cancel',
        'feedback.success': 'Thanks! Your email client will open to send the message.',
        'feedback.error': 'Please write a message before sending.',
        'terms.title': 'Terms of use',
        'terms.p1': 'By using Gestix you agree to use the application responsibly and in compliance with applicable law.',
        'terms.p2': 'Gestix is a personal productivity tool. You are responsible for the content of your tasks and projects.',
        'terms.p3': 'We reserve the right to update these terms. Continued use of the service implies acceptance.',
        'terms.p4': 'The service is provided "as is", without implied warranties of uninterrupted availability.',
        'privacy.title': 'Privacy policy',
        'privacy.p1': 'We only collect information needed for your account: username, email and the tasks/projects you create.',
        'privacy.p2': 'Your data is stored securely and is not sold to third parties.',
        'privacy.p3': 'If you sign in with Google or Microsoft, we use your email to identify your account per those providers\' policies.',
        'privacy.p4': 'You may request deletion of your account and data by contacting us via the feedback form.',
        'legal.close': 'Close',
        'priority.high': 'High',
        'priority.medium': 'Medium',
        'priority.low': 'Low',
        'status.pending': 'Pending',
        'status.progress': 'In progress',
        'status.completed': 'Completed',
        'status.overdue': 'Overdue',
        'chart.priority.high': 'High',
        'chart.priority.medium': 'Medium',
        'chart.priority.low': 'Low',
        'user.default': 'User'
    }
};

function getLang() {
    return localStorage.getItem('gestix_lang') || 'es';
}

function t(key, params = {}) {
    const lang = getLang();
    const dict = TRANSLATIONS[lang] || TRANSLATIONS.es;
    let str = dict[key] || TRANSLATIONS.es[key] || key;
    Object.entries(params).forEach(([k, v]) => {
        str = str.replace(`{${k}}`, v);
    });
    return str;
}

function applyI18n() {
    document.documentElement.lang = getLang();

    document.querySelectorAll('[data-i18n]').forEach(el => {
        el.textContent = t(el.getAttribute('data-i18n'));
    });

    document.querySelectorAll('[data-i18n-placeholder]').forEach(el => {
        el.placeholder = t(el.getAttribute('data-i18n-placeholder'));
    });

    const greet = document.getElementById('greetName');
    if (greet) {
        const name = localStorage.getItem('username') || t('user.default');
        const h2 = greet.closest('h2');
        if (h2) h2.innerHTML = t('tasks.greeting', { name: `<span id="greetName">${name}</span>` });
    }

    if (typeof window._refreshHeaderTitle === 'function') {
        window._refreshHeaderTitle();
    }
    if (typeof window._refreshChartLabels === 'function') {
        window._refreshChartLabels();
    }
    if (typeof window._onTasksLoaded === 'function' && window._allTasks) {
        window._onTasksLoaded(window._allTasks);
    }
}

function setLang(lang) {
    localStorage.setItem('gestix_lang', lang);
    applyI18n();
    document.querySelectorAll('.lang-btn').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.lang === lang);
    });
}
window.t = t;
window.getLang = getLang;
window.setLang = setLang;
window.applyI18n = applyI18n;

(function initLang() {
    const lang = getLang();
    document.querySelectorAll('.lang-btn').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.lang === lang);
    });
})();
