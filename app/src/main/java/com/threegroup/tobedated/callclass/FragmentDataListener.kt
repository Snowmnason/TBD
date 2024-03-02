package com.threegroup.tobedated.callclass

interface FragmentDataListener {
    fun onFragmentDataEntered(data: String, index:Int)
    fun onPhotoDataEntered(photoIndex: Int, uri: String)
}