package com.raizlabs.android.dbflow.config;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.runtime.BaseTransactionManager;
import com.raizlabs.android.dbflow.runtime.ModelNotifier;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;
import com.raizlabs.android.dbflow.structure.database.OpenHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 */
public final class DatabaseConfig {

    public interface OpenHelperCreator {

        OpenHelper createHelper(DatabaseDefinition databaseDefinition, DatabaseHelperListener helperListener);
    }

    public interface TransactionManagerCreator {

        BaseTransactionManager createManager(DatabaseDefinition databaseDefinition);
    }

    private final OpenHelperCreator openHelperCreator;
    private final Class<?> databaseClass;
    private final TransactionManagerCreator transactionManagerCreator;
    private final DatabaseHelperListener helperListener;
    private final Map<Class<?>, TableConfig> tableConfigMap;
    private final ModelNotifier modelNotifier;


    DatabaseConfig(Builder builder) {
        openHelperCreator = builder.openHelperCreator;
        databaseClass = builder.databaseClass;
        transactionManagerCreator = builder.transactionManagerCreator;
        helperListener = builder.helperListener;
        tableConfigMap = builder.tableConfigMap;
        modelNotifier = builder.modelNotifier;
    }

    @Nullable
    public OpenHelperCreator helperCreator() {
        return openHelperCreator;
    }

    @Nullable
    public DatabaseHelperListener helperListener() {
        return helperListener;
    }

    @NonNull
    public Class<?> databaseClass() {
        return databaseClass;
    }

    @Nullable
    public TransactionManagerCreator transactionManagerCreator() {
        return transactionManagerCreator;
    }

    @Nullable
    public ModelNotifier modelNotifier() {
        return modelNotifier;
    }

    @NonNull
    public Map<Class<?>, TableConfig> tableConfigMap() {
        return tableConfigMap;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <TModel> TableConfig<TModel> getTableConfigForTable(Class<TModel> modelClass) {
        return tableConfigMap().get(modelClass);
    }

    public static final class Builder {

        OpenHelperCreator openHelperCreator;
        final Class<?> databaseClass;
        TransactionManagerCreator transactionManagerCreator;
        DatabaseHelperListener helperListener;
        final Map<Class<?>, TableConfig> tableConfigMap = new HashMap<>();
        ModelNotifier modelNotifier;

        public Builder(@NonNull Class<?> databaseClass) {
            this.databaseClass = databaseClass;
        }

        public Builder transactionManagerCreator(TransactionManagerCreator transactionManager) {
            this.transactionManagerCreator = transactionManager;
            return this;
        }

        public Builder helperListener(DatabaseHelperListener helperListener) {
            this.helperListener = helperListener;
            return this;
        }

        public Builder addTableConfig(TableConfig<?> tableConfig) {
            tableConfigMap.put(tableConfig.tableClass(), tableConfig);
            return this;
        }

        public Builder modelNotifier(ModelNotifier modelNotifier) {
            this.modelNotifier = modelNotifier;
            return this;
        }

        /**
         * Overrides the default {@link OpenHelper} for a {@link DatabaseDefinition}.
         *
         * @param openHelper The openhelper to use.
         */
        public Builder openHelper(OpenHelperCreator openHelper) {
            openHelperCreator = openHelper;
            return this;
        }

        public DatabaseConfig build() {
            return new DatabaseConfig(this);
        }
    }
}
