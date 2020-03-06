package org.revolut.dropwizard;

import com.google.inject.AbstractModule;

import org.revolut.dao.AccountDao;
import org.revolut.dao.TransactionDao;
import org.revolut.resource.AccountResource;
import org.revolut.resource.TransactionResource;
import org.revolut.service.impl.AccountServiceImpl;
import org.revolut.service.impl.TransactionServiceImpl;

public class AppGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountResource.class);
        bind(TransactionResource.class);
        bind(AccountServiceImpl.class);
        bind(TransactionServiceImpl.class);
        bind(AccountDao.class);
        bind(TransactionDao.class);
    }
}
