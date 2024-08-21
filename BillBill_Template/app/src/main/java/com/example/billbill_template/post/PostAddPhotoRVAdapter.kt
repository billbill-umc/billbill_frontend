import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.billbill_template.R

class PostAddPhotoRVAdapter(
    private val context: Context,
    private val photoList: MutableList<Uri>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PostAddPhotoRVAdapter.PhotoViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    inner class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.post_add_photo_item_iv)
        val deleteImageView: ImageView = view.findViewById(R.id.post_add_photo_item_delete_iv)

        init {
            imageView.setOnClickListener {
                itemClickListener.onItemClick(adapterPosition)
            }
            deleteImageView.setOnClickListener {
                itemClickListener.onDeleteClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_post_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val imageUri = photoList[position]
        holder.imageView.setImageURI(imageUri)

        // 기본 이미지일 경우 삭제 버튼 숨기기
        if (position == 0 && imageUri.toString().contains("img_post_add_photo")) {
            holder.deleteImageView.visibility = View.GONE
        } else {
            holder.deleteImageView.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}