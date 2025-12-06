import React, { useState, useEffect, useCallback } from 'react';
import { LogIn, UserPlus, Home, LogOut, Loader2, User, Key, Mail } from 'lucide-react';

// URL base de tu API Spring Boot
// ¡CRÍTICO! Asegúrate de que el backend esté corriendo en 8080 para que estas peticiones funcionen.
const API_BASE_URL = 'http://localhost:8080/api/auth';

// Componente principal de la aplicación
const App = () => {
    // Estado para gestionar la autenticación y la información del usuario
    const [auth, setAuth] = useState({
        isAuthenticated: false,
        token: null,
        user: null, // Contiene idUsuario, nombre, email, y roles
    });
    // Estado para gestionar la vista actual (simulacion de ruteo con switch/case)
    const [view, setView] = useState('home');
    const [message, setMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    // Cargar datos de autenticacion desde localStorage al inicio
    useEffect(() => {
        const storedAuth = localStorage.getItem('auth');
        if (storedAuth) {
            try {
                const parsedAuth = JSON.parse(storedAuth);
                // Si hay token y usuario, asumimos que la sesion es valida (para esta demo)
                if (parsedAuth.token && parsedAuth.user) {
                    setAuth(parsedAuth);
                    setView('profile'); 
                }
            } catch (error) {
                console.error("Error al parsear auth de localStorage:", error);
                localStorage.removeItem('auth');
            }
        }
    }, []);

    // Funcion para mostrar mensajes temporales
    const displayMessage = useCallback((msg, duration = 3000) => {
        setMessage(msg);
        const timer = setTimeout(() => setMessage(''), duration);
        return () => clearTimeout(timer);
    }, []);

    // Manejador de Login (Se comunica con /api/auth/login)
    const handleLogin = async (credentials) => {
        setIsLoading(true);
        displayMessage(''); 
        
        try {
            const response = await fetch(`${API_BASE_URL}/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                // El backend espera 'username' y 'password'
                body: JSON.stringify({
                    username: credentials.username,
                    password: credentials.password
                }),
            });

            const data = await response.json();

            if (response.ok) {
                const newAuth = {
                    isAuthenticated: true,
                    token: data.token,
                    user: {
                        idUsuario: data.id,
                        nombre: data.username,
                        email: data.email,
                        roles: data.roles || [],
                    },
                };
                setAuth(newAuth);
                localStorage.setItem('auth', JSON.stringify(newAuth));
                setView('profile');
                displayMessage('¡Inicio de sesión exitoso!');
            } else {
                const errorMessage = data.message || "Credenciales inválidas o Error en el servidor.";
                displayMessage(`Error de Login: ${errorMessage}`);
            }
        } catch (error) {
            console.error('Error al conectar con el backend:', error);
            displayMessage('Error de conexión. Asegúrate de que el backend esté corriendo en 8080.');
        } finally {
            setIsLoading(false);
        }
    };

    // Manejador de Registro (Se comunica con /api/auth/register)
    const handleSignup = async (userData) => {
        setIsLoading(true);
        displayMessage(''); 

        try {
            // El backend espera username, email, y password
            const response = await fetch(`${API_BASE_URL}/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData),
            });

            const data = await response.json();

            if (response.ok) {
                displayMessage('¡Registro exitoso! Por favor, inicia sesión.');
                setView('login');
            } else {
                const errorMessage = data.message || "Error al registrar usuario. El nombre de usuario o email podrían estar en uso.";
                displayMessage(`Error de Registro: ${errorMessage}`);
            }
        } catch (error) {
            console.error('Error al conectar con el backend:', error);
            displayMessage('Error de conexión. Asegúrate de que el backend esté corriendo en 8080.');
        } finally {
            setIsLoading(false);
        }
    };

    // Manejador de Logout
    const handleLogout = () => {
        setAuth({ isAuthenticated: false, token: null, user: null });
        localStorage.removeItem('auth');
        setView('home');
        displayMessage('Sesión cerrada correctamente.');
    };

    // --- Componente de Mensajes Flotantes ---
    const FloatingMessage = () => (
        <div className={`fixed top-4 left-1/2 transform -translate-x-1/2 z-50 transition-opacity duration-300 
            ${message ? 'opacity-100' : 'opacity-0 pointer-events-none'}`}>
            <div className="bg-indigo-600 text-white px-4 py-2 rounded-lg shadow-xl font-medium">
                {message}
            </div>
        </div>
    );

    // --- Componente de Formulario Base para Login y Registro ---
    const AuthForm = ({ title, fields, onSubmit, submitText, isSignup = false }) => {
        const initialFormState = fields.reduce((acc, field) => ({ ...acc, [field.name]: '' }), {});
        const [form, setForm] = useState(initialFormState);

        const handleChange = (e) => {
            setForm({ ...form, [e.target.name]: e.target.value });
        };

        const handleSubmit = (e) => {
            e.preventDefault();
            onSubmit(form);
        };

        return (
            <div className="w-full max-w-md p-6 bg-white rounded-xl shadow-2xl">
                <h2 className="text-3xl font-bold text-center text-indigo-700 mb-6">{title}</h2>
                <form onSubmit={handleSubmit} className="space-y-4">
                    {fields.map((field) => (
                        <div key={field.name}>
                            <label htmlFor={field.name} className="block text-sm font-medium text-gray-700">
                                {field.label}
                            </label>
                            <div className="relative mt-1 rounded-lg shadow-sm">
                                <span className="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-400">
                                    {field.icon === 'User' ? <User size={18} /> : field.icon === 'Key' ? <Key size={18} /> : <Mail size={18} />}
                                </span>
                                <input
                                    id={field.name}
                                    name={field.name}
                                    type={field.type}
                                    required
                                    value={form[field.name]}
                                    onChange={handleChange}
                                    className="block w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500 transition duration-150"
                                />
                            </div>
                        </div>
                    ))}
                    <button
                        type="submit"
                        disabled={isLoading}
                        className="w-full flex justify-center py-3 px-4 border border-transparent rounded-lg shadow-md text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition duration-200 disabled:opacity-50"
                    >
                        {isLoading ? <Loader2 className="animate-spin mr-2" size={20} /> : null}
                        {submitText}
                    </button>
                </form>
                {isSignup ? (
                    <p className="mt-4 text-center text-sm text-gray-600">
                        ¿Ya tienes cuenta?{' '}
                        <button onClick={() => setView('login')} className="font-medium text-indigo-600 hover:text-indigo-500">
                            Inicia Sesión
                        </button>
                    </p>
                ) : (
                    <p className="mt-4 text-center text-sm text-gray-600">
                        ¿No tienes cuenta?{' '}
                        <button onClick={() => setView('signup')} className="font-medium text-indigo-600 hover:text-indigo-500">
                            Regístrate
                        </button>
                    </p>
                )}
            </div>
        );
    };

    // --- Componente de Navegación ---
    const Navbar = () => (
        <nav className="bg-white shadow-md">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16">
                    <div className="flex items-center">
                        <div className="text-2xl font-bold text-indigo-700 tracking-wider">Greenprintshop</div>
                    </div>
                    <div className="flex items-center space-x-2">
                        <button onClick={() => setView('home')} className="flex items-center px-3 py-2 text-sm font-medium text-gray-700 hover:text-indigo-600 rounded-lg transition">
                            <Home className="mr-1" size={20} /> Inicio
                        </button>
                        {!auth.isAuthenticated ? (
                            <>
                                <button onClick={() => setView('login')} className="flex items-center px-3 py-2 text-sm font-medium text-gray-700 hover:text-indigo-600 rounded-lg transition">
                                    <LogIn className="mr-1" size={20} /> Login
                                </button>
                                <button onClick={() => setView('signup')} className="flex items-center px-3 py-2 text-sm font-medium text-gray-700 hover:text-indigo-600 rounded-lg transition">
                                    <UserPlus className="mr-1" size={20} /> Registro
                                </button>
                            </>
                        ) : (
                            <>
                                <button onClick={() => setView('profile')} className="flex items-center px-3 py-2 text-sm font-medium text-gray-700 hover:text-indigo-600 rounded-lg transition">
                                    <User className="mr-1" size={20} /> Perfil ({auth.user.nombre})
                                </button>
                                <button onClick={handleLogout} className="flex items-center px-3 py-2 text-sm font-medium text-gray-700 hover:text-red-600 rounded-lg transition">
                                    <LogOut className="mr-1" size={20} /> Salir
                                </button>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );

    // --- Vistas de la Aplicacion ---

    const HomeView = () => (
        <div className="text-center p-8 bg-white rounded-xl shadow-2xl max-w-lg w-full">
            <h1 className="text-4xl font-extrabold text-indigo-700 mb-4">Bienvenido a Greenprintshop</h1>
            <p className="text-lg text-gray-600 mb-6">Esta es la interfaz de usuario de prueba para tu API de Spring Boot.</p>
            {!auth.isAuthenticated ? (
                <div className="space-y-3">
                    <p className="text-indigo-600 font-semibold">Por favor, inicia sesión o regístrate para continuar.</p>
                    <div className="flex justify-center space-x-4">
                        <button 
                            onClick={() => setView('login')}
                            className="flex items-center px-6 py-2 border border-transparent text-sm font-medium rounded-lg shadow-sm text-white bg-green-600 hover:bg-green-700 transition"
                        >
                            <LogIn className="mr-2" size={20} /> Login
                        </button>
                        <button 
                            onClick={() => setView('signup')}
                            className="flex items-center px-6 py-2 border border-transparent text-sm font-medium rounded-lg shadow-sm text-indigo-600 bg-indigo-100 hover:bg-indigo-200 transition"
                        >
                            <UserPlus className="mr-2" size={20} /> Registro
                        </button>
                    </div>
                </div>
            ) : (
                <div className="space-y-3">
                    <p className="text-green-600 font-semibold">¡Hola, {auth.user.nombre}! Estás autenticado.</p>
                    <button 
                        onClick={() => setView('profile')}
                        className="flex items-center mx-auto px-6 py-2 border border-transparent text-sm font-medium rounded-lg shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 transition"
                    >
                        <User className="mr-2" size={20} /> Ir a Perfil
                    </button>
                </div>
            )}
        </div>
    );

    const LoginView = () => (
        <AuthForm
            title="Iniciar Sesión"
            fields={[
                { name: 'username', label: 'Nombre de Usuario', type: 'text', icon: 'User' },
                { name: 'password', label: 'Contraseña', type: 'password', icon: 'Key' },
            ]}
            onSubmit={handleLogin}
            submitText="Iniciar Sesión"
        />
    );
    
    const SignupView = () => (
        <AuthForm
            title="Crear Cuenta"
            fields={[
                { name: 'username', label: 'Nombre de Usuario', type: 'text', icon: 'User' },
                { name: 'email', label: 'Email', type: 'email', icon: 'Mail' },
                { name: 'password', label: 'Contraseña', type: 'password', icon: 'Key' },
            ]}
            onSubmit={handleSignup}
            submitText="Registrarse"
            isSignup={true}
        />
    );

    const ProfileView = () => (
        <div className="w-full max-w-md p-6 bg-white rounded-xl shadow-2xl">
            <h2 className="text-3xl font-bold text-center text-indigo-700 mb-6 flex items-center justify-center">
                <User className="mr-3" size={30} /> Perfil de Usuario
            </h2>
            <div className="space-y-4">
                <p className="text-lg">
                    <span className="font-semibold text-gray-700">ID:</span> <span className="text-gray-600">{auth.user.idUsuario}</span>
                </p>
                <p className="text-lg">
                    <span className="font-semibold text-gray-700">Nombre:</span> <span className="text-gray-600">{auth.user.nombre}</span>
                </p>
                <p className="text-lg">
                    <span className="font-semibold text-gray-700">Email:</span> <span className="text-gray-600">{auth.user.email}</span>
                </p>
                <div className="mt-4 pt-4 border-t border-gray-200">
                    <span className="font-semibold text-gray-700 block mb-2">Roles:</span>
                    <div className="flex flex-wrap gap-2">
                        {/* Se asume que los roles vienen como un array de strings */}
                        {auth.user.roles.map(role => (
                            <span key={role} className="px-3 py-1 text-sm font-medium text-white bg-green-500 rounded-full">
                                {role}
                            </span>
                        ))}
                    </div>
                </div>
            </div>
            <button 
                onClick={handleLogout}
                className="w-full mt-8 flex items-center justify-center py-2 px-4 border border-transparent rounded-lg shadow-md text-sm font-medium text-white bg-red-600 hover:bg-red-700 transition duration-200"
            >
                <LogOut className="mr-2" size={20} /> Cerrar Sesión
            </button>
        </div>
    );

    // --- Renderizado Principal ---
    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <Navbar />
            <FloatingMessage />
            
            <main className="flex-grow flex items-center justify-center p-4">
                {/* Router simple basado en el estado 'view' */}
                {(() => {
                    switch (view) {
                        case 'home':
                            return <HomeView />;
                        case 'login':
                            return <LoginView />;
                        case 'signup':
                            return <SignupView />;
                        case 'profile':
                            if (auth.isAuthenticated && auth.user) {
                                return <ProfileView />;
                            }
                            // Si no esta autenticado o no hay datos, redirigir a Home
                            return <HomeView />;
                        default:
                            return <HomeView />;
                    }
                })()}
            </main>
        </div>
    );
};

export default App;
