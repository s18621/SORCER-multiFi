package edu.pjatk.inn.coffeemaker;

import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

public interface CoffeeDBService {
    public Context addRecipe(Context context) throws RemoteException, ContextException;

    public Context editRecipe(Context context) throws RemoteException, ContextException;

    public Context deleteRecipe(Context context) throws RemoteException, ContextException;
}
