package by.cisza.smartlistproto.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import by.cisza.smartlistproto.data.db.dao.ReceiptDao
import by.cisza.smartlistproto.data.db.dao.ReceiptItemDao
import by.cisza.smartlistproto.data.db.dao.SmartRecordDao
import by.cisza.smartlistproto.data.db.entities.DbReceipt
import by.cisza.smartlistproto.data.db.entities.DbReceiptItem
import by.cisza.smartlistproto.data.db.entities.DbSmartRecord

@Database(entities = [DbSmartRecord::class, DbReceipt::class, DbReceiptItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun smartRecordDao(): SmartRecordDao

    abstract fun receiptDao(): ReceiptDao

    abstract fun receiptItemDao(): ReceiptItemDao

//    companion object {
//        private var instance: AppDatabase? = null
//
//        operator fun invoke(context: Context): AppDatabase {
//
//            return instance
//                ?: createInstance(
//                    context
//                ).also {
//                    instance = it
//                }
//        }
//
//        private fun createInstance(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                AppDatabase::class.java,
//                "smart_list_proto.db"
//            ).build()
//    }
}