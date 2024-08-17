package com.example.billbill_template.ui.home

import com.example.billbill_template.post.GetCategoryManifestResponse
import com.example.billbill_template.post.GetPostsData

interface HomeView {
    fun onGetPostsSuccess(result: GetPostsData)
    fun onGetPostsFailure(message: String)

    fun onGetCategoriesSuccess(result: GetCategoryManifestResponse)
    fun onGetCategoriesFailure(message: String)
}