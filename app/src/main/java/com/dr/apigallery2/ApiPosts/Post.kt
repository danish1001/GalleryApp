package com.dr.apigallery2.ApiPosts

public class Post {

    private var photos: PhotosObject? = null
    private var stat: String? = null

    constructor(photos: PhotosObject?, stat: String?) {
        this.photos = photos
        this.stat = stat
    }

    fun getPhotos(): PhotosObject? {
        return photos
    }

    fun setPhotos(photos: PhotosObject?) {
        this.photos = photos
    }

    fun getStat(): String? {
        return stat
    }

    fun setStat(stat: String?) {
        this.stat = stat
    }

}













