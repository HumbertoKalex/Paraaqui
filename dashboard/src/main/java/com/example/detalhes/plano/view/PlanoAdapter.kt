package com.example.detalhes.plano.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dashboard.databinding.ItemPlanoBinding

data class Plan(
    val id: String,
    val name: String,
    val price: String,
    val description: String,
    val features: List<String>
)

class PlanoAdapter(
    private val plans: List<Plan>,
    private val onPlanSelected: (Plan) -> Unit
) : RecyclerView.Adapter<PlanoAdapter.PlanViewHolder>() {

    inner class PlanViewHolder(private val binding: ItemPlanoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plan: Plan) {
            binding.planName.text = plan.name
            binding.planPrice.text = plan.price
            binding.planDescription.text = plan.description
            binding.planFeature1.text = plan.features[0]
            binding.planFeature2.text = plan.features[1]

            binding.btnSubscribe.setOnClickListener {
                onPlanSelected(plan)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = ItemPlanoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(plans[position])
    }

    override fun getItemCount(): Int = plans.size
}
