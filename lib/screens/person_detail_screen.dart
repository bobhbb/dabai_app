import "package:flutter/material.dart";
import "package:provider/provider.dart";
import "../providers/app_state.dart";
import "../models/person_model.dart";
import "../models/health_record_model.dart";
import "../widgets/relationship_avatar.dart";
import "../widgets/health_score_bar.dart";
import "../config/constants.dart";

class PersonDetailScreen extends StatefulWidget {
  final String personId;
  const PersonDetailScreen({super.key, required this.personId});

  @override
  State<PersonDetailScreen> createState() => _PersonDetailScreenState();
}

class _PersonDetailScreenState extends State<PersonDetailScreen> {
  PersonModel? _person;
  List<HealthRecordModel> _records = [];
  bool _loaded = false;

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    final state = context.read<AppState>();
    final db = state;
    await db.loadRecords(widget.personId);
    if (!mounted) return;
    setState(() {
      _person = db.persons.firstWhere((p) => p.personId == widget.personId);
      _records = db.records;
      _loaded = true;
    });
  }

  @override
  Widget build(BuildContext context) {
    if (!_loaded) return const Scaffold(body: Center(child: CircularProgressIndicator()));
    final person = _person!;
    final healthColor = person.healthScore >= 75 ? const Color(DabaiColors.healthGood) :
                        person.healthScore >= 55 ? const Color(DabaiColors.healthWarn) :
                        const Color(DabaiColors.healthBad);

    return Scaffold(
      backgroundColor: const Color(DabaiColors.background),
      appBar: AppBar(
        title: const Text(""),
        actions: [IconButton(icon: const Icon(Icons.camera_alt), onPressed: () => Navigator.pushNamed(context, "/camera"))],
      ),
      body: Column(
        children: [
          Card(
            margin: const EdgeInsets.symmetric(horizontal: 16),
            child: Padding(
              padding: const EdgeInsets.all(20),
              child: Row(
                children: [
                  RelationshipAvatar(relationship: person.relationship, nickname: person.nickname, size: 72),
                  const SizedBox(width: 20),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(person.nickname, style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold)),
                        Text(person.relationship, style: const TextStyle(fontSize: 14, color: Color(DabaiColors.subtext))),
                        Text("已检测 ${person.recordCount} 次", style: const TextStyle(fontSize: 12, color: Color(DabaiColors.subtext))),
                      ],
                    ),
                  ),
                  Column(
                    children: [
                      Text("${person.healthScore.toInt()}", style: TextStyle(fontSize: 32, fontWeight: FontWeight.bold, color: healthColor)),
                      const Text("分", style: TextStyle(fontSize: 14, color: Color(DabaiColors.subtext))),
                    ],
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),
          Row(
            children: [
              const SizedBox(width: 16),
              Expanded(
                child: OutlinedButton.icon(
                  onPressed: () => Navigator.pushNamed(context, "/todo", arguments: {"personId": widget.personId}),
                  icon: const Icon(Icons.checklist),
                  label: const Text("行动计划"),
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: ElevatedButton.icon(
                  onPressed: () => Navigator.pushNamed(context, "/camera"),
                  icon: const Icon(Icons.camera_alt),
                  label: const Text("重新检测"),
                  style: ElevatedButton.styleFrom(backgroundColor: const Color(DabaiColors.primary)),
                ),
              ),
              const SizedBox(width: 16),
            ],
          ),
          const SizedBox(height: 16),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20),
            child: Row(
              children: [
                const Text("健康记录", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                const Spacer(),
                TextButton.icon(
                  onPressed: () {},
                  icon: const Icon(Icons.history, size: 18),
                  label: const Text("查看全部"),
                ),
              ],
            ),
          ),
          Expanded(
            child: _records.isEmpty
                ? const Center(child: Text("暂无记录", style: TextStyle(color: Color(DabaiColors.subtext))))
                : ListView.builder(
                    itemCount: _records.length,
                    itemBuilder: (ctx, i) => Card(
                      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
                      child: ListTile(
                        leading: Container(
                          width: 40, height: 40,
                          decoration: BoxDecoration(color: const Color(DabaiColors.primaryLight), borderRadius: BorderRadius.circular(10)),
                          child: const Icon(Icons.description, color: Color(DabaiColors.primary)),
                        ),
                        title: Text("第 ${_records.length - i} 次健康报告"),
                        subtitle: Text(_fmtDate(_records[i].analyzedAt), style: const TextStyle(fontSize: 12)),
                        trailing: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            Text("${_records[i].healthScore.toInt()}", style: TextStyle(fontWeight: FontWeight.bold, color: _records[i].healthScore >= 75 ? const Color(DabaiColors.healthGood) : const Color(DabaiColors.healthWarn))),
                            const Icon(Icons.chevron_right, color: Color(DabaiColors.subtext)),
                          ],
                        ),
                        onTap: () => Navigator.pushNamed(context, "/health-report", arguments: {
                          "personId": widget.personId,
                          "recordId": _records[i].recordId,
                        }),
                      ),
                    ),
                  ),
          ),
        ],
      ),
    );
  }

  String _fmtDate(int ms) {
    final dt = DateTime.fromMillisecondsSinceEpoch(ms);
    return "${dt.year}/${dt.month}/${dt.day} ${dt.hour.toString().padLeft(2, "0")}:${dt.minute.toString().padLeft(2, "0")}";
  }
}
