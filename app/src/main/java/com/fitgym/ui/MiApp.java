package com.fitgym.ui;

import android.app.Application;
import android.util.Log;

import com.fitgym.core.DBManager;

public class MiApp extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            this.dbManager = new DBManager( this.getApplicationContext() );
        }
        public DBManager getBD() {
            return this.dbManager;
        }
        private DBManager dbManager;

}
