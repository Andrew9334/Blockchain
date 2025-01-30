import { useEffect, useState } from "react";

export default function ThemeToggle() {
    const [theme, setTheme] = useState(localStorage.getItem("theme") || "light");

    useEffect(() => {
        if (theme === "dark") {
            document.documentElement.classList.add("dark");
        } else {
            document.documentElement.classList.remove("dark");
        }
        localStorage.setItem("theme", theme);
    }, [theme]);

    return (
        <button
            onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
            className="px-4 py-2 rounded-lg bg-gray-800 dark:bg-gray-200 text-white dark:text-black transition-all"
        >
            {theme === "dark" ? "☀️ Светлая тема" : "🌙 Тёмная тема"}
        </button>
    );
}
