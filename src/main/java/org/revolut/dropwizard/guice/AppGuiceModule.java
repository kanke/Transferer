package org.revolut.dropwizard.guice;

import com.google.inject.AbstractModule;

import com.google.inject.Singleton;
import org.revolut.dao.impl.InMemoryAccountDao;
import org.revolut.dao.AccountDao;
import org.revolut.resource.AccountResource;
import org.revolut.resource.TransactionResource;
import org.revolut.service.AccountService;
import org.revolut.service.AccountTransactionService;
import org.revolut.service.impl.AccountServiceImpl;
import org.revolut.service.impl.AccountTransactionServiceImpl;

public class AppGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountResource.class);
        bind(TransactionResource.class);
        bind(AccountTransactionServiceImpl.class);
        bind(AccountService.class).to(AccountServiceImpl.class);
        bind(AccountTransactionService.class).to(AccountTransactionServiceImpl.class);
        bind(AccountDao.class).to(InMemoryAccountDao.class).in(Singleton.class);
    }
}
