package com.example.pencatatansembako

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class SembakoDaoTest {

    private lateinit var db: SembakoDatabase
    private lateinit var sembakoDao: SembakoDao

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, SembakoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        sembakoDao = db.sembakoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertSembako_and_getAll_shouldReturnInsertedItem() = runBlocking {
        val sembako = Sembako(nama = "Beras", jumlah = 5.0, harga = 60000.0)
        sembakoDao.insert(sembako)

        val allItems = sembakoDao.getAll()
        assertEquals(1, allItems.size)
        assertEquals("Beras", allItems[0].nama)
        assertEquals(5.0, allItems[0].jumlah, 0.001)
        assertEquals(60000.0, allItems[0].harga, 0.001)
    }

    @Test
    fun deleteSembako_shouldRemoveItem() = runBlocking {
        val sembako = Sembako(nama = "Gula", jumlah = 2.0, harga = 15000.0)
        sembakoDao.insert(sembako)
        val inserted = sembakoDao.getAll().first()

        sembakoDao.delete(inserted)
        val allItems = sembakoDao.getAll()
        assertTrue(allItems.isEmpty())
    }
}
