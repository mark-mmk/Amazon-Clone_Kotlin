package com.example.amazon.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [CartItem::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    object DatabaseBuilder {
        @Volatile
        private var INSTANCE: AppDataBase? = null
        fun getInstance(context: Context): AppDataBase {
            if (INSTANCE == null) {
                synchronized(AppDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "db_products"
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE!!
        }
    }

}