package edu.pjatk.inn.coffeemaker.impl;

import edu.pjatk.inn.coffeemaker.AppUIService;
import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

public class AppUIImpl implements AppUIService {
    @Override
    public Context makeBeverage(Context context) throws RemoteException, ContextException {
        return context;
    }

    @Override
    public Context chooseBeverage(Context context) throws RemoteException, ContextException {
        return context;
    }
}
