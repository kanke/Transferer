package org.revolut.dropwizard;

import com.google.inject.AbstractModule;

import com.google.inject.Singleton;
import org.revolut.dao.InMemoryAccountDao;
import org.revolut.dao.AccountDao;
import org.revolut.resource.AccountResource;
import org.revolut.resource.TransactionResource;
import org.revolut.service.AccountService;
import org.revolut.service.TransactionService;

public class AppGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountResource.class);
        bind(TransactionResource.class);
        bind(TransactionService.class);
        bind(AccountService.class);
        bind(TransactionService.class);
        bind(AccountDao.class).to(InMemoryAccountDao.class).in(Singleton.class);
    }
}
