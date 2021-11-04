package com.example.practica1.base

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class dbHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${FeedReaderContract.FeedEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedReaderContract.FeedEntry.COLUMN_NAME_NAME} TEXT," +
                "${FeedReaderContract.FeedEntry.COLUMN_NAME_TOKEN} TEXT," +
                "${FeedReaderContract.FeedEntry.COLUMN_NAME_IDUSR} INTEGER)"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry.TABLE_NAME}"

    object FeedReaderContract {
        // Table contents are grouped together in an anonymous object.
        object FeedEntry : BaseColumns {
            const val TABLE_NAME = "loginaccess"
            const val COLUMN_NAME_NAME = "nameuser"
            const val COLUMN_NAME_TOKEN = "token"
            const val COLUMN_NAME_IDUSR = "idusr"
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "bd_app_venta_2.db"
    }
}