package edu.pjatk.inn.coffeemaker.impl;

import edu.pjatk.inn.coffeemaker.CoffeeMachineAppService;
import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

public class CoffeeMachineAppImpl implements CoffeeMachineAppService {
    @Override
    public Context getRecipe(Context context) throws RemoteException, ContextException {
        return context;
    }

    @Override
    public Context checkInventory(Context context) throws RemoteException, ContextException {
        return context;
    }
}
