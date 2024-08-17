package com.example.billbill_template.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.billbill_template.databinding.DialogCalendarBinding

interface PostCalendarDialogInterface {
    fun onClickOkButton(id : Int)
}
class PostCalendarDialog (postCalendarDialogInterface: PostCalendarDialogInterface
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogCalendarBinding? = null
    private val binding get() = _binding!!

    private var postCalendarDialogInterface: PostCalendarDialogInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogCalendarBinding.inflate(inflater, container, false)
        val view = binding.root

//        // 레이아웃 배경을 투명하게 해줌, 필수 아님
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


//        // 취소 버튼 클릭
//        binding.dialogNoBtn.setOnClickListener {
//            dismiss()
//        }

        // 확인 버튼 클릭
        binding.postCalendarOkTv.setOnClickListener {
            this.postCalendarDialogInterface?.onClickOkButton(id!!)
            dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}