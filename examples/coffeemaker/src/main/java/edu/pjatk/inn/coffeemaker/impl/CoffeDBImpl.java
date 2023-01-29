package edu.pjatk.inn.coffeemaker.impl;

import edu.pjatk.inn.coffeemaker.CoffeeDBService;
import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

public class CoffeDBImpl implements CoffeeDBService {
    @Override
    public Context addRecipe(Context context) throws RemoteException, ContextException {
        return context;
    }

    @Override
    public Context editRecipe(Context context) throws RemoteException, ContextException {
        return context;
    }

    @Override
    public Context deleteRecipe(Context context) throws RemoteException, ContextException {
        return context;
    }
}
