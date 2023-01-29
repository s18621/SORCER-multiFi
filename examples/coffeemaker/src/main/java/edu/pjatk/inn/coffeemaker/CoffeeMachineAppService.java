package edu.pjatk.inn.coffeemaker;

import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

public interface CoffeeMachineAppService {
    public Context getRecipe(Context context) throws RemoteException, ContextException;

    public Context checkInventory(Context context) throws RemoteException, ContextException;
}
