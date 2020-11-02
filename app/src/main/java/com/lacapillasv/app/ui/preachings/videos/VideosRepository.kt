package com.lacapillasv.app.ui.preachings.videos

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lacapillasv.app.model.Video
import com.lacapillasv.app.ui.readingclub.ReadingClubRepository
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class VideosRepository {

    companion object {
        const val VideosCollectionName = "Videos"
        const val TAG = "VideosRepository"
    }

    suspend fun getVideos() = suspendCoroutine<List<Video>> { cont ->
        val db = Firebase.firestore
        db.collection(VideosCollectionName)
            .orderBy("release_date")
            .get()
            .addOnSuccessListener { documents ->
                val videos = mutableListOf<Video>()
                documents.forEach { document ->
                    val video = document.toObject(Video::class.java).withId<Video>(document.id)
                    val release = (document.data["release_date"] as com.google.firebase.Timestamp).toDate()
                    video.releaseDate = release
                    videos.add(video)
                }
                cont.resume(videos)
            }
            .addOnFailureListener { exception ->
                Log.w(ReadingClubRepository.TAG, "Error getting documents: ", exception)
                cont.resumeWithException(exception)
            }
    }

}