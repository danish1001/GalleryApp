package com.dr.apigallery2.ApiPosts

class PhotosArray {

    private var id: String? = null
    private var owner: String? = null
    private var secret: String? = null
    private var server: String? = null
    private var farm = 0
    private var title: String? = null
    private var ispublic = 0
    private var isfriend = 0
    private var isfamily = 0
    private var url_s: String? = null
    private var height_s = 0
    private var width_s = 0

    fun PhotosArray(
        id: String?,
        owner: String?,
        secret: String?,
        server: String?,
        farm: Int,
        title: String?,
        ispublic: Int,
        isfriend: Int,
        isfamily: Int,
        url_s: String?,
        height_s: Int,
        width_s: Int
    ) {
        this.id = id
        this.owner = owner
        this.secret = secret
        this.server = server
        this.farm = farm
        this.title = title
        this.ispublic = ispublic
        this.isfriend = isfriend
        this.isfamily = isfamily
        this.url_s = url_s
        this.height_s = height_s
        this.width_s = width_s
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getOwner(): String? {
        return owner
    }

    fun setOwner(owner: String?) {
        this.owner = owner
    }

    fun getSecret(): String? {
        return secret
    }

    fun setSecret(secret: String?) {
        this.secret = secret
    }

    fun getServer(): String? {
        return server
    }

    fun setServer(server: String?) {
        this.server = server
    }

    fun getFarm(): Int {
        return farm
    }

    fun setFarm(farm: Int) {
        this.farm = farm
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun getIspublic(): Int {
        return ispublic
    }

    fun setIspublic(ispublic: Int) {
        this.ispublic = ispublic
    }

    fun getIsfriend(): Int {
        return isfriend
    }

    fun setIsfriend(isfriend: Int) {
        this.isfriend = isfriend
    }

    fun getIsfamily(): Int {
        return isfamily
    }

    fun setIsfamily(isfamily: Int) {
        this.isfamily = isfamily
    }

    fun getUrl_s(): String? {
        return url_s
    }

    fun setUrl_s(url_s: String?) {
        this.url_s = url_s
    }

    fun getHeight_s(): Int {
        return height_s
    }

    fun setHeight_s(height_s: Int) {
        this.height_s = height_s
    }

    fun getWidth_s(): Int {
        return width_s
    }

    fun setWidth_s(width_s: Int) {
        this.width_s = width_s
    }

}