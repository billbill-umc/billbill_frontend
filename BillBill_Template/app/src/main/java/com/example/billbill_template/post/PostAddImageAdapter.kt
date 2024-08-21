package com.example.billbill_template.post

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.billbill_template.R

class PostAddImageAdapter(private val images: MutableList<Uri>) : RecyclerView.Adapter< PostAddImageAdapter.ImageViewHolder>() {

    private var isDefaultImage = false

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.post_add_photo_item_iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_add_post_photo, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageURI(images[position])
    }

    override fun getItemCount(): Int = images.size

    fun addImage(imageUri: Uri, isDefault: Boolean = false) {
        if (isDefault) {
            // 기본 이미지 추가
            images.add(imageUri)
            isDefaultImage = true
        } else {
            images.add(imageUri)
            isDefaultImage = false
        }
        notifyDataSetChanged()
    }

    fun removeDefaultImage() {
        if (isDefaultImage) {
            images.clear()
            isDefaultImage = false
            notifyDataSetChanged()
        }
    }

    fun removeImage(position: Int) {
        images.removeAt(position)
        if (images.isEmpty()) {
            // 모든 이미지가 제거되었을 때 기본 이미지 추가
            val defaultImageUri = Uri.parse("android.resource://BillBill_Template/drawable/image_post_add_photo")
            addImage(defaultImageUri, isDefault = true)
        }
        notifyDataSetChanged()
    }
}