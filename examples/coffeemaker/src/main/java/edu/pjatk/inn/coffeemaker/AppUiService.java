package edu.pjatk.inn.coffeemaker;

import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

public interface AppUiService {
    public Context makeBeverage(Context context) throws RemoteException, ContextException;

    public Context chooseBeverage(Context context) throws RemoteException, ContextException;
}
