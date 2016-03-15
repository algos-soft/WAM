package it.algos.wam.login;

import com.vaadin.server.Page;

import java.net.URI;

/**
 * Created by alex on 15-03-2016.
 */
public class Login {

    public static void login(){
        URI uri = URI.create("/");
        Page.getCurrent().setLocation(uri);
        Page.getCurrent().reload();
    }


    private String getHomeAddress(){
        URI uri = Page.getCurrent().getLocation();
        String str = uri.getAuthority()+uri.getPath();
        return str;
    }


}
