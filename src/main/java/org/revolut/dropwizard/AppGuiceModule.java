package org.revolut.dropwizard;

import com.google.inject.AbstractModule;

import org.revolut.dao.AccountDao;
import org.revolut.dao.TransactionDao;
import org.revolut.resource.AccountResource;
import org.revolut.resource.TransactionResource;
import org.revolut.service.AccountService;
import org.revolut.service.TransactionService;

public class AppGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountResource.class);
        bind(TransactionResource.class);
        bind(AccountService.class);
        bind(TransactionService.class);
        bind(AccountDao.class);
        bind(TransactionDao.class);
    }


}