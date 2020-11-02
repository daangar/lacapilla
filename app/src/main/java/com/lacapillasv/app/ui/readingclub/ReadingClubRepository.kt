package com.lacapillasv.app.ui.readingclub

import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lacapillasv.app.model.Book
import com.lacapillasv.app.model.BookType
import com.lacapillasv.app.model.Guide
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ReadingClubRepository {

    companion object {
        const val BooksCollectionName = "Books"
        const val GuidesCollectionName = "Guides"
        const val TAG = "ReadingClubRepository"
    }

    suspend fun getBooks(bookType: BookType) = suspendCoroutine<List<Book>>{ continuation ->
        val db = Firebase.firestore
        db.collection(BooksCollectionName)
            .whereEqualTo("type", bookType.toString())
            .get()
            .addOnSuccessListener { documents ->
                val books = mutableListOf<Book>()
                documents.forEach { document ->

                    val book = document.toObject(Book::class.java).withId<Book>(document.id)
                    book.type = BookType.valueOf(document.data["type"].toString())
                    books.add(book)
                }
                continuation.resume(books)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                continuation.resumeWithException(exception)
            }
    }

    suspend fun getGuides(bookId: String) = suspendCoroutine<List<Guide>> { cont ->
        val db = Firebase.firestore
        db.collection(BooksCollectionName)
            .document(bookId)
            .collection(GuidesCollectionName)
            .orderBy("release_date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val guides = mutableListOf<Guide>()
                documents.forEach {document ->
                    val guide = document.toObject(Guide::class.java).withId<Guide>(document.id)
                    val release = (document.data["release_date"] as com.google.firebase.Timestamp).toDate()
                    guide.releaseDate = release
                    guide.fileReference = document.data["file_reference"].toString()
                    guides.add(guide)
                }
                cont.resume(guides)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                cont.resumeWithException(exception)
            }
    }

}