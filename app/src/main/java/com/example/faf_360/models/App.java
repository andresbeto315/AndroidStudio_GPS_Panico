package com.example.faf_360.models;

public class App {

    private static App app;

    private App()
    {
        // Usuario por defecto que esta autenticado
    }

    public static App GetApp()
    {
        if (app == null)
            app = new App();
        return app;
    }

    private Usuarios userLogin;

    public Usuarios getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(Usuarios userLogin) {
        this.userLogin = userLogin;
    }
}