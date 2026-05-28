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
        'tasks.filter.pending': 'Pendiente',
        'tasks.filter.progress': 'En progreso',
        'tasks.filter.completed': 'Completadas',
        'tasks.filter.overdue': 'Vencidas',
        'tasks.viewAll': 'Ver todas las tareas →',
        'tasks.stat.total': 'Total Tareas',
        'tasks.stat.pending': 'Pendiente',
        'tasks.stat.progress': 'En Progreso',
        'tasks.stat.completed': 'Completadas',
        'tasks.stat.overdue': 'Vencidas',
        'tasks.stat.loading': 'Cargando...',
        'tasks.stat.focus': 'Enfócate 💪',
        'tasks.stat.noTasksYet': 'Sin tareas aún',
        'tasks.stat.totalCount': '{n} en total',
        'tasks.stat.newPending': '+{n} nuevas',
        'tasks.stat.allCaughtUp': 'Al día 🎉',
        'tasks.stat.completedCount': '+{n} completadas',
        'tasks.stat.keepGoing': 'Sigue adelante 💪',
        'tasks.stat.needAttention': 'Requieren atención',
        'tasks.stat.allOnTrack': 'Todo al día ✅',
        'tasks.chart.weekly': 'Progreso semanal',
        'tasks.chart.thisWeek': 'Esta semana',
        'tasks.chart.completed': 'Completado',
        'tasks.chart.completedLabel': 'Completadas',
        'tasks.chart.inProgress': 'En progreso',
        'tasks.chart.pending': 'Pendiente',
        'tasks.chart.overdue': 'Vencidas',
        'tasks.chart.byPriority': 'Por prioridad',
        'tasks.upcoming': 'Próximas tareas',
        'tasks.upcoming.viewCal': 'Ver calendario →',
        'tasks.upcoming.empty': 'No hay tareas próximas 🎉',
        'tasks.empty.create': '+ Crear tarea',
        'tasks.empty.title': 'No hay tareas aquí',
        'tasks.empty.subtitle': 'Crea tu primera tarea para empezar',
        'tasks.empty.noResults': 'Sin resultados',
        'tasks.empty.noResultsFor': 'No se encontraron tareas para "{q}"',
        'tasks.empty.noOverdue': 'Ninguna tarea vencida',
        'tasks.empty.noOverdueSub': 'Vas al día con todas tus fechas límite',
        'tasks.empty.noStatus': 'No hay tareas {status}',
        'tasks.empty.changeFilter': 'Cambia el filtro o crea una nueva tarea',
        'tasks.empty.noPriority': 'No hay tareas de prioridad {priority}',
        'tasks.empty.tryOtherPriority': 'Prueba con otro filtro de prioridad',
        'tasks.empty.noInProject': 'No hay tareas en {name}',
        'tasks.empty.assignProject': 'Crea una tarea y asígnale este proyecto',
        'tasks.checkbox.complete': 'Marcar como completada',
        'tasks.checkbox.uncomplete': 'Marcar como pendiente',
        'tasks.count': '{n} tareas',
        'tasks.countOne': '{n} tarea',
        'tasks.projectLabel': 'Proyecto: {name}',
        'projects.title': 'Proyectos 📁',
        'projects.subtitle': 'Tus tareas organizadas por proyecto o categoría.',
        'projects.create': 'Crear nuevo proyecto',
        'projects.empty.title': 'No hay proyectos aún',
        'projects.empty.subtitle': 'Crea tu primer proyecto para organizar tus tareas',
        'projects.back': 'Volver a proyectos',
        'projects.delete': 'Eliminar proyecto',
        'projects.newTaskHere': 'Nueva tarea aquí',
        'projects.progress': 'Progreso del proyecto',
        'projects.noTasksFilter': 'No hay tareas en este filtro',
        'projects.createInProject': '+ Crear tarea en este proyecto',
        'projects.completedTodo': '{done} completadas · {todo} por hacer',
        'projects.overdueBadge': '⚠️ {n} vencida',
        'projects.overdueBadgeMany': '⚠️ {n} vencidas',
        'projects.deleteTitle': 'Eliminar proyecto',
        'projects.noProject': 'Sin proyecto',
        'calendar.title': 'Calendario 📅',
        'calendar.subtitle': 'Visualiza todas tus tareas por fecha.',
        'calendar.selectDay': 'Selecciona un día',
        'calendar.clickDay': 'Haz click en un día para ver sus tareas',
        'calendar.noTasksDay': 'No hay tareas para este día',
        'calendar.createTask': '+ Crear tarea',
        'calendar.addTask': '+ Agregar tarea',
        'calendar.noTasks': 'Sin tareas',
        'calendar.moreTasks': '+{n} más',
        'calendar.dayOf': '{day} de {month} {year}',
        'calendar.weekday.sun': 'Dom',
        'calendar.weekday.mon': 'Lun',
        'calendar.weekday.tue': 'Mar',
        'calendar.weekday.wed': 'Mié',
        'calendar.weekday.thu': 'Jue',
        'calendar.weekday.fri': 'Vie',
        'calendar.weekday.sat': 'Sáb',
        'calendar.month.0': 'Enero', 'calendar.month.1': 'Febrero', 'calendar.month.2': 'Marzo',
        'calendar.month.3': 'Abril', 'calendar.month.4': 'Mayo', 'calendar.month.5': 'Junio',
        'calendar.month.6': 'Julio', 'calendar.month.7': 'Agosto', 'calendar.month.8': 'Septiembre',
        'calendar.month.9': 'Octubre', 'calendar.month.10': 'Noviembre', 'calendar.month.11': 'Diciembre',
        'reports.title': 'Reportes 📊',
        'reports.subtitle': 'Analiza tu productividad y el estado de tus tareas.',
        'reports.kpi.weekCompleted': 'Completadas esta semana',
        'reports.kpi.avgClose': 'Tiempo medio de cierre',
        'reports.kpi.avgCloseSub': 'días desde creación',
        'reports.kpi.onTime': 'Tasa de cumplimiento',
        'reports.kpi.onTimeSub': 'sin vencer al completar',
        'reports.kpi.overdueActive': 'Tareas vencidas activas',
        'reports.kpi.overduePct': '{n}% de tareas activas con fecha',
        'reports.kpi.noActiveDue': 'Sin tareas activas con fecha',
        'reports.delta.up': '+{n} vs semana anterior',
        'reports.delta.down': '{n} vs semana anterior',
        'reports.delta.same': 'Igual que la semana anterior',
        'reports.chart.weeklyCompleted': 'Completadas por semana',
        'reports.chart.last8weeks': 'Últimas 8 semanas',
        'reports.chart.topProjects': 'Proyectos más activos',
        'reports.chart.top5': 'Top 5 por número de tareas',
        'reports.chart.statusOverview': 'Estado general',
        'reports.chart.statusDistribution': 'Distribución actual de tus tareas',
        'reports.chart.projectSummary': 'Resumen por proyecto',
        'reports.chart.thisWeek': 'Esta sem.',
        'reports.table.empty': 'No hay proyectos con tareas aún',
        'reports.table.row': '{total} tareas · {done} completadas',
        'reports.table.overdue': '{n} vencidas',
        'modal.task.new': 'Nueva Tarea',
        'modal.task.edit': 'Editar Tarea',
        'modal.task.detail': 'Detalles de la tarea',
        'modal.task.title': 'Título',
        'modal.task.titlePlaceholder': '¿Qué hay que hacer?',
        'modal.task.description': 'Descripción',
        'modal.task.descriptionPlaceholder': 'Detalles opcionales...',
        'modal.task.status': 'Estado',
        'modal.task.priority': 'Prioridad',
        'modal.task.category': 'Categoría / Proyecto',
        'modal.task.categoryPlaceholder': 'Trabajo, Personal...',
        'modal.task.dueDate': 'Fecha límite',
        'modal.task.save': 'Guardar tarea',
        'modal.task.editBtn': 'Editar tarea',
        'modal.task.project': 'Proyecto',
        'modal.task.created': 'Creada',
        'modal.task.updated': 'Actualizada',
        'modal.project.new': '📁 Nuevo Proyecto',
        'modal.project.name': 'Nombre del proyecto *',
        'modal.project.namePlaceholder': 'ej. Diseño Web, Marketing, App móvil...',
        'modal.project.hint': 'Podrás agregar tareas a este proyecto después de crearlo.',
        'modal.project.create': 'Crear Proyecto',
        'modal.delete.task': 'Eliminar tarea',
        'modal.delete.taskConfirm': 'Esta acción no se puede deshacer.',
        'modal.delete.projectConfirm': '¿Eliminar "{name}"?',
        'modal.delete.projectEmpty': 'Este proyecto no tiene tareas. Se eliminará de tu lista de proyectos.',
        'modal.delete.projectOne': 'Si eliminas este proyecto, se borrará permanentemente 1 tarea que contiene.',
        'modal.delete.projectMany': 'Si eliminas este proyecto, se borrarán permanentemente las {n} tareas que contiene.',
        'modal.delete.projectBtn': 'Eliminar proyecto',
        'modal.delete.deleting': 'Eliminando...',
        'common.cancel': 'Cancelar',
        'common.close': 'Cerrar',
        'common.delete': 'Eliminar',
        'common.save': 'Guardar',
        'common.edit': 'Editar',
        'common.loading': 'Cargando...',
        'common.today': 'Hoy',
        'common.tomorrow': 'Mañana',
        'common.general': 'General',
        'common.noDescription': 'Sin descripción',
        'common.dash': '—',
        'common.error': 'Error',
        'errors.loadTasks': 'Error al cargar tareas',
        'errors.network': 'No se pudo conectar al servidor. Verifica que esté en ejecución.',
        'errors.backToLogin': 'Volver a iniciar sesión',
        'errors.updateTask': 'No se pudo actualizar la tarea',
        'errors.saveTask': 'Error al guardar la tarea',
        'errors.deleteProject': 'No se pudo eliminar el proyecto',
        'errors.deleteProjectTask': 'No se pudo eliminar una tarea del proyecto',
        'errors.projectExists': 'Ya existe un proyecto con ese nombre.',
        'errors.createProject': 'No se pudo crear el proyecto.',
        'errors.connectionProject': 'Error de conexión al crear el proyecto.',
        'count.completed': '{n} completada',
        'count.completedMany': '{n} completadas',
        'count.inProgress': '{n} en progreso',
        'count.pending': '{n} pendiente',
        'count.pendingMany': '{n} pendientes',
        'count.overdue': '{n} vencida',
        'count.overdueMany': '{n} vencidas',
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
        'priority.high': 'Alta',
        'priority.medium': 'Medio',
        'priority.low': 'Baja',
        'priority.highEmoji': '🟢 Baja',
        'status.pending': 'Pendiente',
        'status.progress': 'En progreso',
        'status.completed': 'Completada',
        'status.overdue': 'Vencida',
        'status.pendingLower': 'pendiente',
        'status.progressLower': 'en progreso',
        'status.completedLower': 'completadas',
        'priority.highLower': 'alta',
        'priority.mediumLower': 'medio',
        'priority.lowLower': 'baja',
        'chart.priority.high': 'Alta',
        'chart.priority.medium': 'Medio',
        'chart.priority.low': 'Baja',
        'user.default': 'Usuario',
        'login.title': 'Inicia sesión',
        'login.welcome': '¡Bienvenido de vuelta! 👋',
        'login.subtitle': 'Inicia sesión para continuar',
        'login.email': 'Correo electrónico',
        'login.password': 'Contraseña',
        'login.remember': 'Recordarme',
        'login.forgot': '¿Olvidaste tu contraseña?',
        'login.submit': 'Iniciar sesión',
        'login.orContinue': 'o continúa con',
        'login.google': 'Continuar con Google',
        'login.microsoft': 'Continuar con Microsoft',
        'login.noAccount': '¿No tienes cuenta?',
        'login.registerLink': 'Regístrate aquí',
        'login.backHome': '← Volver al inicio',
        'register.title': 'Crea tu cuenta ✨',
        'register.subtitle': 'Empieza a organizar tus tareas en segundos',
        'register.username': 'Nombre de usuario',
        'register.passwordHint': 'Mínimo 6 caracteres',
        'register.submit': 'Crear cuenta',
        'register.terms': 'Al crear una cuenta aceptas nuestros Términos y Política de Privacidad.',
        'register.hasAccount': '¿Ya tienes cuenta?',
        'register.loginLink': 'Inicia sesión aquí',
        'forgot.title': 'Recuperar contraseña',
        'forgot.subtitle': 'Te enviaremos un enlace para restablecerla',
        'forgot.submit': 'Enviar enlace',
        'forgot.backLogin': '← Volver al inicio de sesión',
        'forgot.oauthError': 'Error al iniciar sesión con Google. Inténtalo de nuevo.',
        'reset.title': 'Restablecer contraseña',
        'reset.pageTitle': 'Nueva contraseña',
        'reset.subtitle': 'Elige una contraseña nueva para tu cuenta',
        'reset.password': 'Nueva contraseña',
        'reset.confirm': 'Confirmar contraseña',
        'reset.submit': 'Restablecer contraseña',
        'reset.backLogin': '← Volver al inicio de sesión',
        'reset.subtitleLong': 'Elige una contraseña segura de al menos 6 caracteres',
        'reset.savePassword': 'Guardar contraseña',
        'reset.goLogin': 'Ir al inicio de sesión',
        'reset.confirmPlaceholder': 'Repite la contraseña',
        'errors.loginInvalid': 'Email o contraseña incorrectos',
        'errors.register': 'Error al registrarse',
        'errors.forgotSend': 'No se pudo enviar el enlace',
        'errors.forgotSuccess': 'Revisa tu correo para continuar.',
        'errors.passwordMismatch': 'Las contraseñas no coinciden',
        'errors.resetInvalidLink': 'Enlace inválido. Solicita uno nuevo desde el login.',
        'errors.resetInvalidExpired': 'Enlace inválido o expirado. Solicita uno nuevo.',
        'errors.resetSuccess': 'Contraseña actualizada.',
        'errors.resetInvalidIncomplete': 'Enlace inválido o incompleto. Solicita uno nuevo desde el login.',
        'index.tagline': 'Organiza tus tareas, proyectos y fechas en un solo lugar.',
        'index.cta': 'Empezar gratis →',
        'index.login': 'Iniciar sesión',
        'index.feature1': 'Tareas y proyectos',
        'index.feature1desc': 'Organiza todo por categorías y prioridades',
        'index.feature2': 'Calendario visual',
        'index.feature2desc': 'Visualiza fechas límite de un vistazo',
        'index.feature3': 'Reportes',
        'index.feature3desc': 'Mide tu productividad semana a semana',
        'lang.toggle': 'EN'
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
        'tasks.stat.noTasksYet': 'No tasks yet',
        'tasks.stat.totalCount': '{n} in total',
        'tasks.stat.newPending': '+{n} new',
        'tasks.stat.allCaughtUp': 'All caught up 🎉',
        'tasks.stat.completedCount': '+{n} completed',
        'tasks.stat.keepGoing': 'Keep going 💪',
        'tasks.stat.needAttention': 'Need attention',
        'tasks.stat.allOnTrack': 'All on track ✅',
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
        'tasks.empty.title': 'No tasks here',
        'tasks.empty.subtitle': 'Create your first task to get started',
        'tasks.empty.noResults': 'No results',
        'tasks.empty.noResultsFor': 'No tasks found for "{q}"',
        'tasks.empty.noOverdue': 'No overdue tasks',
        'tasks.empty.noOverdueSub': 'You are on track with all due dates',
        'tasks.empty.noStatus': 'No {status} tasks',
        'tasks.empty.changeFilter': 'Change the filter or create a new task',
        'tasks.empty.noPriority': 'No {priority} priority tasks',
        'tasks.empty.tryOtherPriority': 'Try another priority filter',
        'tasks.empty.noInProject': 'No tasks in {name}',
        'tasks.empty.assignProject': 'Create a task and assign it to this project',
        'tasks.checkbox.complete': 'Mark as completed',
        'tasks.checkbox.uncomplete': 'Mark as pending',
        'tasks.count': '{n} tasks',
        'tasks.countOne': '{n} task',
        'tasks.projectLabel': 'Project: {name}',
        'projects.title': 'Projects 📁',
        'projects.subtitle': 'Your tasks organized by project or category.',
        'projects.create': 'Create new project',
        'projects.empty.title': 'No projects yet',
        'projects.empty.subtitle': 'Create your first project to organize your tasks',
        'projects.back': 'Back to projects',
        'projects.delete': 'Delete project',
        'projects.newTaskHere': 'New task here',
        'projects.progress': 'Project progress',
        'projects.noTasksFilter': 'No tasks in this filter',
        'projects.createInProject': '+ Create task in this project',
        'projects.completedTodo': '{done} completed · {todo} to do',
        'projects.overdueBadge': '⚠️ {n} overdue',
        'projects.overdueBadgeMany': '⚠️ {n} overdue',
        'projects.deleteTitle': 'Delete project',
        'projects.noProject': 'No project',
        'calendar.title': 'Calendar 📅',
        'calendar.subtitle': 'View all your tasks by date.',
        'calendar.selectDay': 'Select a day',
        'calendar.clickDay': 'Click a day to see its tasks',
        'calendar.noTasksDay': 'No tasks for this day',
        'calendar.createTask': '+ Create task',
        'calendar.addTask': '+ Add task',
        'calendar.noTasks': 'No tasks',
        'calendar.moreTasks': '+{n} more',
        'calendar.dayOf': '{month} {day}, {year}',
        'calendar.weekday.sun': 'Sun',
        'calendar.weekday.mon': 'Mon',
        'calendar.weekday.tue': 'Tue',
        'calendar.weekday.wed': 'Wed',
        'calendar.weekday.thu': 'Thu',
        'calendar.weekday.fri': 'Fri',
        'calendar.weekday.sat': 'Sat',
        'calendar.month.0': 'January', 'calendar.month.1': 'February', 'calendar.month.2': 'March',
        'calendar.month.3': 'April', 'calendar.month.4': 'May', 'calendar.month.5': 'June',
        'calendar.month.6': 'July', 'calendar.month.7': 'August', 'calendar.month.8': 'September',
        'calendar.month.9': 'October', 'calendar.month.10': 'November', 'calendar.month.11': 'December',
        'reports.title': 'Reports 📊',
        'reports.subtitle': 'Analyze your productivity and task status.',
        'reports.kpi.weekCompleted': 'Completed this week',
        'reports.kpi.avgClose': 'Average close time',
        'reports.kpi.avgCloseSub': 'days from creation',
        'reports.kpi.onTime': 'On-time rate',
        'reports.kpi.onTimeSub': 'completed before due date',
        'reports.kpi.overdueActive': 'Active overdue tasks',
        'reports.kpi.overduePct': '{n}% of active tasks with due date',
        'reports.kpi.noActiveDue': 'No active tasks with due date',
        'reports.delta.up': '+{n} vs last week',
        'reports.delta.down': '{n} vs last week',
        'reports.delta.same': 'Same as last week',
        'reports.chart.weeklyCompleted': 'Completed per week',
        'reports.chart.last8weeks': 'Last 8 weeks',
        'reports.chart.topProjects': 'Most active projects',
        'reports.chart.top5': 'Top 5 by task count',
        'reports.chart.statusOverview': 'Overall status',
        'reports.chart.statusDistribution': 'Current distribution of your tasks',
        'reports.chart.projectSummary': 'Summary by project',
        'reports.chart.thisWeek': 'This wk',
        'reports.table.empty': 'No projects with tasks yet',
        'reports.table.row': '{total} tasks · {done} completed',
        'reports.table.overdue': '{n} overdue',
        'modal.task.new': 'New Task',
        'modal.task.edit': 'Edit Task',
        'modal.task.detail': 'Task details',
        'modal.task.title': 'Title',
        'modal.task.titlePlaceholder': 'What needs to be done?',
        'modal.task.description': 'Description',
        'modal.task.descriptionPlaceholder': 'Optional details...',
        'modal.task.status': 'Status',
        'modal.task.priority': 'Priority',
        'modal.task.category': 'Category / Project',
        'modal.task.categoryPlaceholder': 'Work, Personal...',
        'modal.task.dueDate': 'Due date',
        'modal.task.save': 'Save task',
        'modal.task.editBtn': 'Edit task',
        'modal.task.project': 'Project',
        'modal.task.created': 'Created',
        'modal.task.updated': 'Updated',
        'modal.project.new': '📁 New Project',
        'modal.project.name': 'Project name *',
        'modal.project.namePlaceholder': 'e.g. Web Design, Marketing, Mobile app...',
        'modal.project.hint': 'You can add tasks to this project after creating it.',
        'modal.project.create': 'Create Project',
        'modal.delete.task': 'Delete task',
        'modal.delete.taskConfirm': 'This action cannot be undone.',
        'modal.delete.projectConfirm': 'Delete "{name}"?',
        'modal.delete.projectEmpty': 'This project has no tasks. It will be removed from your project list.',
        'modal.delete.projectOne': 'If you delete this project, 1 task it contains will be permanently deleted.',
        'modal.delete.projectMany': 'If you delete this project, all {n} tasks it contains will be permanently deleted.',
        'modal.delete.projectBtn': 'Delete project',
        'modal.delete.deleting': 'Deleting...',
        'common.cancel': 'Cancel',
        'common.close': 'Close',
        'common.delete': 'Delete',
        'common.save': 'Save',
        'common.edit': 'Edit',
        'common.loading': 'Loading...',
        'common.today': 'Today',
        'common.tomorrow': 'Tomorrow',
        'common.general': 'General',
        'common.noDescription': 'No description',
        'common.dash': '—',
        'common.error': 'Error',
        'errors.loadTasks': 'Error loading tasks',
        'errors.network': 'Could not connect to the server. Make sure it is running.',
        'errors.backToLogin': 'Back to sign in',
        'errors.updateTask': 'Could not update the task',
        'errors.saveTask': 'Error saving the task',
        'errors.deleteProject': 'Could not delete the project',
        'errors.deleteProjectTask': 'Could not delete a project task',
        'errors.projectExists': 'A project with that name already exists.',
        'errors.createProject': 'Could not create the project.',
        'errors.connectionProject': 'Connection error while creating the project.',
        'count.completed': '{n} completed',
        'count.completedMany': '{n} completed',
        'count.inProgress': '{n} in progress',
        'count.pending': '{n} pending',
        'count.pendingMany': '{n} pending',
        'count.overdue': '{n} overdue',
        'count.overdueMany': '{n} overdue',
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
        'priority.highEmoji': '🟢 Low',
        'status.pending': 'Pending',
        'status.progress': 'In progress',
        'status.completed': 'Completed',
        'status.overdue': 'Overdue',
        'status.pendingLower': 'pending',
        'status.progressLower': 'in progress',
        'status.completedLower': 'completed',
        'priority.highLower': 'high',
        'priority.mediumLower': 'medium',
        'priority.lowLower': 'low',
        'chart.priority.high': 'High',
        'chart.priority.medium': 'Medium',
        'chart.priority.low': 'Low',
        'user.default': 'User',
        'login.title': 'Sign in',
        'login.welcome': 'Welcome back! 👋',
        'login.subtitle': 'Sign in to continue',
        'login.email': 'Email',
        'login.password': 'Password',
        'login.remember': 'Remember me',
        'login.forgot': 'Forgot your password?',
        'login.submit': 'Sign in',
        'login.orContinue': 'or continue with',
        'login.google': 'Continue with Google',
        'login.microsoft': 'Continue with Microsoft',
        'login.noAccount': "Don't have an account?",
        'login.registerLink': 'Sign up here',
        'login.backHome': '← Back to home',
        'register.title': 'Create your account ✨',
        'register.subtitle': 'Start organizing your tasks in seconds',
        'register.username': 'Username',
        'register.passwordHint': 'At least 6 characters',
        'register.submit': 'Create account',
        'register.terms': 'By creating an account you agree to our Terms and Privacy Policy.',
        'register.hasAccount': 'Already have an account?',
        'register.loginLink': 'Sign in here',
        'forgot.title': 'Reset password',
        'forgot.subtitle': 'We will send you a link to reset it',
        'forgot.submit': 'Send link',
        'forgot.backLogin': '← Back to sign in',
        'forgot.oauthError': 'Error signing in with Google. Please try again.',
        'reset.title': 'Reset password',
        'reset.pageTitle': 'New password',
        'reset.subtitle': 'Choose a new password for your account',
        'reset.password': 'New password',
        'reset.confirm': 'Confirm password',
        'reset.submit': 'Reset password',
        'reset.backLogin': '← Back to sign in',
        'reset.subtitleLong': 'Choose a secure password with at least 6 characters',
        'reset.savePassword': 'Save password',
        'reset.goLogin': 'Go to sign in',
        'reset.confirmPlaceholder': 'Repeat password',
        'errors.loginInvalid': 'Incorrect email or password',
        'errors.register': 'Registration error',
        'errors.forgotSend': 'Could not send the link',
        'errors.forgotSuccess': 'Check your email to continue.',
        'errors.passwordMismatch': 'Passwords do not match',
        'errors.resetInvalidLink': 'Invalid link. Request a new one from the login page.',
        'errors.resetInvalidExpired': 'Invalid or expired link. Request a new one.',
        'errors.resetSuccess': 'Password updated.',
        'errors.resetInvalidIncomplete': 'Invalid or incomplete link. Request a new one from the login page.',
        'index.tagline': 'Organize your tasks, projects and dates in one place.',
        'index.cta': 'Start free →',
        'index.login': 'Sign in',
        'index.feature1': 'Tasks & projects',
        'index.feature1desc': 'Organize everything by category and priority',
        'index.feature2': 'Visual calendar',
        'index.feature2desc': 'See due dates at a glance',
        'index.feature3': 'Reports',
        'index.feature3desc': 'Track your productivity week by week',
        'lang.toggle': 'ES'
    }
};

const NO_PROJECT_LEGACY = 'Sin proyecto';

function getLang() {
    return 'es';
}

function getLocale() {
    return 'es-ES';
}

function t(key, params = {}) {
    const lang = getLang();
    const dict = TRANSLATIONS[lang] || TRANSLATIONS.es;
    let str = dict[key] || TRANSLATIONS.es[key] || key;
    Object.entries(params).forEach(([k, v]) => {
        str = str.replace(new RegExp(`\\{${k}\\}`, 'g'), v);
    });
    return str;
}

function tStatus(code, lower = false) {
    const map = {
        PENDING: lower ? 'status.pendingLower' : 'status.pending',
        IN_PROGRESS: lower ? 'status.progressLower' : 'status.progress',
        COMPLETED: lower ? 'status.completedLower' : 'status.completed'
    };
    return t(map[code] || 'status.pending');
}

function tPriority(code, lower = false) {
    const map = {
        HIGH: lower ? 'priority.highLower' : 'priority.high',
        MEDIUM: lower ? 'priority.mediumLower' : 'priority.medium',
        LOW: lower ? 'priority.lowLower' : 'priority.low'
    };
    return t(map[code] || 'priority.medium');
}

function tPriorityEmoji(code) {
    const map = { HIGH: '🔴', MEDIUM: '🟡', LOW: '🟢' };
    return `${map[code] || '🟡'} ${tPriority(code)}`;
}

function noProjectLabel() {
    return t('projects.noProject');
}

function isNoProjectName(name) {
    return !name || name === NO_PROJECT_LEGACY || name === noProjectLabel()
        || name === TRANSLATIONS.es['projects.noProject']
        || name === TRANSLATIONS.en['projects.noProject'];
}

function projectCategoryKey(category) {
    if (!category || !String(category).trim()) return NO_PROJECT_LEGACY;
    return category;
}

function projectDisplayName(name) {
    return isNoProjectName(name) ? noProjectLabel() : name;
}

function displayProjectName(cat) {
    if (!cat || !String(cat).trim()) return noProjectLabel();
    return cat;
}

function formatTaskCount(n) {
    return t(n === 1 ? 'tasks.countOne' : 'tasks.count', { n });
}

function formatCount(keySingular, keyPlural, n) {
    return t(n === 1 ? keySingular : keyPlural, { n });
}

function calMonthName(monthIndex) {
    return t(`calendar.month.${monthIndex}`);
}

function calWeekdayShort(index) {
    const keys = ['sun', 'mon', 'tue', 'wed', 'thu', 'fri', 'sat'];
    return t(`calendar.weekday.${keys[index]}`);
}

function formatCalDayTitle(y, m, d) {
    if (getLang() === 'en') {
        return t('calendar.dayOf', { day: d, month: calMonthName(m), year: y });
    }
    return t('calendar.dayOf', { day: d, month: calMonthName(m), year: y });
}

function refreshTaskFormSelects() {
    const statusEl = document.getElementById('taskStatus');
    if (statusEl) {
        ['PENDING', 'IN_PROGRESS', 'COMPLETED'].forEach(val => {
            const opt = statusEl.querySelector(`option[value="${val}"]`);
            if (opt) opt.textContent = tStatus(val);
        });
    }
    const priorityEl = document.getElementById('taskPriority');
    if (priorityEl) {
        ['LOW', 'MEDIUM', 'HIGH'].forEach(val => {
            const opt = priorityEl.querySelector(`option[value="${val}"]`);
            if (opt) opt.textContent = tPriorityEmoji(val);
        });
    }
}

function applyI18n() {
    document.documentElement.lang = 'es';

    document.querySelectorAll('[data-i18n]').forEach(el => {
        if (el.hasAttribute('data-i18n-skip')) return;
        el.setAttribute('translate', 'no');
        el.classList.add('notranslate');
        el.textContent = t(el.getAttribute('data-i18n'));
    });

    document.querySelectorAll('[data-i18n-placeholder]').forEach(el => {
        el.placeholder = t(el.getAttribute('data-i18n-placeholder'));
    });

    document.querySelectorAll('[data-i18n-title]').forEach(el => {
        el.title = t(el.getAttribute('data-i18n-title'));
    });

    document.querySelectorAll('[data-i18n-aria]').forEach(el => {
        el.setAttribute('aria-label', t(el.getAttribute('data-i18n-aria')));
    });

    document.querySelectorAll('option[data-i18n]').forEach(opt => {
        opt.textContent = t(opt.getAttribute('data-i18n'));
    });

    refreshTaskFormSelects();

    const greet = document.getElementById('greetName');
    if (greet) {
        const name = localStorage.getItem('username') || t('user.default');
        const h2 = greet.closest('h2');
        if (h2) h2.innerHTML = t('tasks.greeting', { name: `<span id="greetName">${name}</span>` });
    }

    if (typeof window._refreshHeaderTitle === 'function') {
        window._refreshHeaderTitle();
    }
}

function bootstrapPageI18n() {
    if (window._i18nBootstrapDone) return;
    window._i18nBootstrapDone = true;
    applyI18n();
}

function refreshLocaleUI() {
    try {
        if (typeof window._refreshChartLabels === 'function') {
            window._refreshChartLabels();
        }
        if (typeof window._refreshReportChartLabels === 'function') {
            window._refreshReportChartLabels();
        }
        if (typeof window.refreshLocaleContent === 'function') {
            window.refreshLocaleContent();
        }
    } catch (err) {
        console.error('refreshLocaleUI error:', err);
    }
}

window.t = t;
window.getLang = getLang;
window.getLocale = getLocale;
window.applyI18n = applyI18n;
window.refreshLocaleUI = refreshLocaleUI;
window.bootstrapPageI18n = bootstrapPageI18n;
window.tStatus = tStatus;
window.tPriority = tPriority;
window.tPriorityEmoji = tPriorityEmoji;
window.noProjectLabel = noProjectLabel;
window.isNoProjectName = isNoProjectName;
window.displayProjectName = displayProjectName;
window.formatTaskCount = formatTaskCount;
window.formatCount = formatCount;
window.calMonthName = calMonthName;
window.calWeekdayShort = calWeekdayShort;
window.formatCalDayTitle = formatCalDayTitle;
window.projectCategoryKey = projectCategoryKey;
window.projectDisplayName = projectDisplayName;
window.NO_PROJECT_LEGACY = NO_PROJECT_LEGACY;

(function initLang() {
    document.documentElement.lang = 'es';
    try { localStorage.removeItem('gestix_lang'); } catch (_) { /* ignore */ }
})();

function runI18nBootstrap() {
    bootstrapPageI18n();
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', runI18nBootstrap, { once: true });
} else {
    runI18nBootstrap();
}
