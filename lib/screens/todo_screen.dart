import "package:flutter/material.dart";
import "package:provider/provider.dart";
import "../providers/app_state.dart";
import "../models/action_plan_model.dart";
import "../config/constants.dart";

class TodoScreen extends StatefulWidget {
  final String personId;
  const TodoScreen({super.key, required this.personId});

  @override
  State<TodoScreen> createState() => _TodoScreenState();
}

class _TodoScreenState extends State<TodoScreen> {
  bool _loaded = false;
  List<ActionPlanModel> _plans = [];
  Map<String, List<ActionItemModel>> _planItems = {};

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    final state = context.read<AppState>();
    await state.loadPlans(widget.personId);
    if (!mounted) return;
    setState(() {
      _plans = state.plans;
      _loaded = true;
    });
    _loadItems();
  }

  Future<void> _loadItems() async {
    final state = context.read<AppState>();
    final planItems = <String, List<ActionItemModel>>{};
    for (final plan in _plans) {
      planItems[plan.planId] = await state.getPlanItems(plan.planId);
    }
    if (mounted) setState(() => _planItems = planItems);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(DabaiColors.background),
      appBar: AppBar(title: const Text("行动计划", style: TextStyle(fontWeight: FontWeight.bold))),
      body: !_loaded
          ? const Center(child: CircularProgressIndicator())
          : _plans.isEmpty
              ? const Center(child: Text("暂无行动计划", style: TextStyle(color: Color(DabaiColors.subtext))))
              : ListView.builder(
                  padding: const EdgeInsets.all(16),
                  itemCount: _plans.length,
                  itemBuilder: (ctx, i) => _PlanCard(
                    plan: _plans[i],
                    items: _planItems[_plans[i].planId] ?? [],
                    onToggle: (item) {
                      state.toggleItem(item);
                      _loadItems();
                    },
                  ),
                ),
    );
  }
}

class _PlanCard extends StatelessWidget {
  final ActionPlanModel plan;
  final List<ActionItemModel> items;
  final Function(ActionItemModel) onToggle;
  const _PlanCard({required this.plan, required this.items, required this.onToggle});

  @override
  Widget build(BuildContext context) {
    final done = items.where((i) => i.isChecked).length;
    final total = items.length;
    final freqLabel = plan.frequency == "DAILY" ? "每日" : plan.frequency == "WEEKLY" ? "每周" : plan.frequency == "MONTHLY" ? "每月" : "每年";
    final emoji = plan.frequency == "DAILY" ? "☀️" : plan.frequency == "WEEKLY" ? "📅" : plan.frequency == "MONTHLY" ? "📊" : "🎯";

    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text("$emoji  ${plan.title}", style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                    if (total > 0) Text("$done/$total", style: const TextStyle(fontSize: 12, color: Color(DabaiColors.subtext))),
                  ],
                ),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                  decoration: BoxDecoration(color: const Color(DabaiColors.primaryLight), borderRadius: BorderRadius.circular(12)),
                  child: Text(freqLabel, style: const TextStyle(fontSize: 12, color: Color(DabaiColors.primaryDark), fontWeight: FontWeight.w500)),
                ),
              ],
            ),
            if (total > 0) ...[
              const SizedBox(height: 8),
              ClipRRect(
                borderRadius: BorderRadius.circular(3),
                child: LinearProgressIndicator(
                  value: done / total,
                  minHeight: 6,
                  backgroundColor: const Color(DabaiColors.divider),
                  valueColor: const AlwaysStoppedAnimation<Color>(Color(DabaiColors.healthGood)),
                ),
              ),
            ],
            if (items.isNotEmpty) ...[
              const SizedBox(height: 8),
              ...items.map((item) => _TodoItem(item: item, onToggle: () => onToggle(item))),
            ],
          ],
        ),
      ),
    );
  }
}

class _TodoItem extends StatelessWidget {
  final ActionItemModel item;
  final VoidCallback onToggle;
  const _TodoItem({required this.item, required this.onToggle});

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onToggle,
      child: Padding(
        padding: const EdgeInsets.symmetric(vertical: 4),
        child: Row(
          children: [
            Icon(
              item.isChecked ? Icons.check_circle : Icons.radio_button_unchecked,
              color: item.isChecked ? const Color(DabaiColors.healthGood) : const Color(DabaiColors.subtext),
              size: 22,
            ),
            const SizedBox(width: 8),
            Expanded(
              child: Text(
                item.content,
                style: TextStyle(
                  fontSize: 14,
                  color: item.isChecked ? const Color(DabaiColors.subtext) : const Color(DabaiColors.onSurface),
                  decoration: item.isChecked ? TextDecoration.lineThrough : null,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
