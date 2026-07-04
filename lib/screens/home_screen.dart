import "package:flutter/material.dart";
import "package:provider/provider.dart";
import "../providers/app_state.dart";
import "../models/person_model.dart";
import "../widgets/health_score_bar.dart";
import "../widgets/relationship_avatar.dart";
import "../config/constants.dart";

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final state = context.watch<AppState>();
    return Scaffold(
      backgroundColor: const Color(DabaiColors.background),
      appBar: AppBar(
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text("大白伴家", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 22)),
            Text("守护 ${state.personCount} 位家人", style: const TextStyle(fontSize: 12, color: Color(DabaiColors.subtext))),
          ],
        ),
        actions: [
          IconButton(icon: const Icon(Icons.notifications_outlined), onPressed: () {}),
          IconButton(icon: const Icon(Icons.person_outline), onPressed: () {}),
        ],
      ),
      body: Column(
        children: [
          if (state.canAddPerson)
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 8),
              child: Row(
                children: [
                  const Text("家人席位", style: TextStyle(fontSize: 14, fontWeight: FontWeight.w500)),
                  const SizedBox(width: 12),
                  ...List.generate(3, (i) => Padding(
                    padding: const EdgeInsets.only(right: 6),
                    child: Container(width: 12, height: 12, decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: i < state.personCount ? const Color(DabaiColors.primary) : const Color(DabaiColors.divider),
                    )),
                  )),
                  const Spacer(),
                  Text("${state.personCount}/3", style: const TextStyle(fontSize: 12, color: Color(DabaiColors.subtext))),
                ],
              ),
            ),
          Expanded(
            child: state.persons.isEmpty
                ? _EmptyState(onTap: () => Navigator.pushNamed(context, "/camera"))
                : ListView.builder(
                    padding: const EdgeInsets.all(16),
                    itemCount: state.canAddPerson ? state.persons.length + 1 : state.persons.length,
                    itemBuilder: (ctx, i) {
                      if (state.canAddPerson && i == state.persons.length) {
                        return _AddPersonCard(onTap: () => Navigator.pushNamed(context, "/camera"));
                      }
                      return _PersonCard(
                        person: state.persons[i],
                        onTap: () => Navigator.pushNamed(context, "/person-detail", arguments: {"personId": state.persons[i].personId}),
                        onPhoto: () => Navigator.pushNamed(context, "/camera"),
                      );
                    },
                  ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.large(
        onPressed: () => Navigator.pushNamed(context, "/camera"),
        backgroundColor: const Color(DabaiColors.primary),
        child: const Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(Icons.camera_alt, size: 32, color: Colors.white),
            Text("拍照", style: TextStyle(color: Colors.white, fontSize: 10)),
          ],
        ),
      ),
    );
  }
}

class _EmptyState extends StatelessWidget {
  final VoidCallback onTap;
  const _EmptyState({required this.onTap});
  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Text("🤖", style: TextStyle(fontSize: 80)),
          const SizedBox(height: 16),
          const Text("还没有家人记录", style: TextStyle(fontSize: 18, color: Color(DabaiColors.subtext))),
          const SizedBox(height: 24),
          ElevatedButton.icon(
            onPressed: onTap,
            icon: const Icon(Icons.camera_alt),
            label: const Text("拍张照片，开始健康管理"),
          ),
        ],
      ),
    );
  }
}

class _PersonCard extends StatelessWidget {
  final PersonModel person;
  final VoidCallback onTap;
  final VoidCallback onPhoto;
  const _PersonCard({required this.person, required this.onTap, required this.onPhoto});

  @override
  Widget build(BuildContext context) {
    final healthColor = person.healthScore >= 75 ? const Color(DabaiColors.healthGood) :
                        person.healthScore >= 55 ? const Color(DabaiColors.healthWarn) :
                        const Color(DabaiColors.healthBad);
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      child: InkWell(
        borderRadius: BorderRadius.circular(16),
        onTap: onTap,
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Row(
            children: [
              RelationshipAvatar(relationship: person.relationship, nickname: person.nickname, size: 64),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(person.nickname, style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                    Text(person.relationship, style: const TextStyle(fontSize: 13, color: Color(DabaiColors.subtext))),
                    const SizedBox(height: 8),
                    HealthScoreBar(score: person.healthScore, color: healthColor, height: 6),
                    const SizedBox(height: 4),
                    Text("已检测 ${person.recordCount} 次", style: const TextStyle(fontSize: 11, color: Color(DabaiColors.subtext))),
                  ],
                ),
              ),
              IconButton(onPressed: onPhoto, icon: const Icon(Icons.camera_alt, color: Color(DabaiColors.primary))),
            ],
          ),
        ),
      ),
    );
  }
}

class _AddPersonCard extends StatelessWidget {
  final VoidCallback onTap;
  const _AddPersonCard({required this.onTap});
  @override
  Widget build(BuildContext context) {
    return Card(
      child: InkWell(
        borderRadius: BorderRadius.circular(16),
        onTap: onTap,
        child: Container(
          height: 120,
          alignment: Alignment.center,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Container(
                width: 56, height: 56,
                decoration: const BoxDecoration(shape: BoxShape.circle, color: Color(DabaiColors.primaryLight)),
                child: const Icon(Icons.add, size: 32, color: Color(DabaiColors.primary)),
              ),
              const SizedBox(height: 8),
              const Text("添加家人", style: TextStyle(fontWeight: FontWeight.w500, color: Color(DabaiColors.primary))),
              const Text("最多3位", style: TextStyle(fontSize: 12, color: Color(DabaiColors.subtext))),
            ],
          ),
        ),
      ),
    );
  }
}
