package com.dr.apigallery2.ApiPosts

import com.dr.apigallery2.MainActivity
import com.google.gson.annotations.SerializedName

public class PhotosObject {

    private var page: Int
    private var pages: Int

    @SerializedName("perpage")
    private var perPage: Int

    private var total: Int

    @SerializedName("photo")
    private var photosArrays: Array<PhotosArray>


    constructor(
        page: Int,
        pages: Int,
        perPage: Int,
        total: Int,
        photosArrays: Array<PhotosArray>
    ) {
        this.page = page
        this.pages = pages
        this.perPage = perPage
        this.total = total
        this.photosArrays = photosArrays
    }

    fun getPage(): Int {
        return page
    }

    fun setPage(page: Int) {
        this.page = page
    }

    fun getPages(): Int {
        return pages
    }

    fun setPages(pages: Int) {
        this.pages = pages
    }

    fun getPerPage(): Int {
        return perPage
    }

    fun setPerPage(perPage: Int) {
        this.perPage = perPage
    }

    fun getTotal(): Int {
        return total
    }

    fun setTotal(total: Int) {
        this.total = total
    }

    fun getPhotosArrays(): Array<PhotosArray>? {
        return photosArrays
    }

    fun setPhotosArrays(photosArrays: Array<PhotosArray>) {
        this.photosArrays = photosArrays
    }

}