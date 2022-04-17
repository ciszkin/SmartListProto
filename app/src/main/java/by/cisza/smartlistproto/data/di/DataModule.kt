package by.cisza.smartlistproto.data.di

import android.content.Context
import androidx.room.Room
import by.cisza.smartlistproto.data.db.AppDatabase
import by.cisza.smartlistproto.data.db.dao.ReceiptDao
import by.cisza.smartlistproto.data.db.dao.ReceiptItemDao
import by.cisza.smartlistproto.data.db.dao.SmartRecordDao
import by.cisza.smartlistproto.data.domain.smartrecord.GetSmartRecordsUseCase
import by.cisza.smartlistproto.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "smart_list_proto.db"
        ).build()
    }

    @Provides
    fun provideSmartRecordDao(database: AppDatabase) : SmartRecordDao {
        return database.smartRecordDao()
    }

    @Provides
    fun provideReceiptDao(database: AppDatabase) : ReceiptDao {
        return database.receiptDao()
    }

    @Provides
    fun provideReceiptItemDao(database: AppDatabase) : ReceiptItemDao {
        return database.receiptItemDao()
    }

    @Provides
    fun provideSmartRecordStore(smartRecordDao: SmartRecordDao) : SmartRecordStore {
        return SmartRecordStore(smartRecordDao)
    }

    @Provides
    fun provideReceiptStore(receiptDao: ReceiptDao) : ReceiptStore {
        return ReceiptStore(receiptDao)
    }

    @Provides
    fun provideReceiptItemStore(receiptItemDao: ReceiptItemDao) : ReceiptItemStore {
        return ReceiptItemStore(receiptItemDao)
    }

    @Provides
    fun providesRepository(
        smartRecordStore: SmartRecordStore,
        receiptStore: ReceiptStore,
        receiptItemStore: ReceiptItemStore
    ) : AppRepository {
        return Repository(
            recordStore = smartRecordStore,
            receiptStore = receiptStore,
            receiptItemStore = receiptItemStore
        )
    }

}