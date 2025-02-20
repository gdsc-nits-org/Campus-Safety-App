import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campussafetyapp.ResourceItem
import com.example.campussafetyapp.databinding.ListItemAcademicsBinding
import com.example.campussafetyapp.databinding.ListItemBinding

class ResourceAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<ResourceAdapter.ViewHolder>() {

    private var resourceList = listOf<ResourceItem>()

    fun submitList(newList: List<ResourceItem>) {
        resourceList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemAcademicsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resourceList[position], position, onClick)
    }

    override fun getItemCount(): Int = resourceList.size

    class ViewHolder(private val binding: ListItemAcademicsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResourceItem, position: Int, onClick: (String) -> Unit) {
            binding.courseCode.text = item.courseCode
            binding.courseName.text = item.courseName

            // Set alternating background colors
            val color = if (position % 2 == 0)
                Color.parseColor("#D2EFFF")  // Light blue
            else
                Color.parseColor("#67B0FA")  // Dark blue

            // Apply alternating color as CardView background
            binding.root.setCardBackgroundColor(color)

            // Click event
            binding.root.setOnClickListener { onClick(item.link) }
        }
    }
}
