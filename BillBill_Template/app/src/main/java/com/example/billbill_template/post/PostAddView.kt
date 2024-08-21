package com.example.billbill_template.post

interface PostAddView {
    fun onGetCategoriesSuccess(result: GetCategoryManifestResponse)
    fun onGetCategoriesFailure(message: String)
}