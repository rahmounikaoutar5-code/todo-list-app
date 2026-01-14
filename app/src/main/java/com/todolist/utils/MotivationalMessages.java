package com.todolist.utils;

import java.util.Random;

public class MotivationalMessages {
    
    private static final String[] MESSAGES = {
        "🎯 Chaque petite tâche accomplie vous rapproche de vos objectifs!",
        "💪 La persévérance est la clé du succès!",
        "✨ Vous êtes capable de grandes choses!",
        "🚀 Un pas à la fois, vous y arriverez!",
        "🌟 Le succès est la somme de petits efforts répétés!",
        "🔥 L'énergie que vous mettez aujourd'hui crée votre demain!",
        "🌈 Après la pluie vient le beau temps, continuez!",
        "⚡ La motivation vous amène au départ, l'habitude vous mène à l'arrivée!",
        "🏆 Chaque journée est une nouvelle opportunité de briller!",
        "🌱 Les grands arbres ont commencé par de petites graines!",
        "🎨 Créez votre journée comme une œuvre d'art!",
        "📈 Le progrès, pas la perfection!",
        "🌄 Le soleil se lève pour ceux qui osent agir!",
        "🧠 Votre esprit est votre outil le plus puissant!",
        "🌟 Vous avez déjà fait tellement de chemin!",
        "🚀 Prêt pour une journée productive?",
        "💫 Votre potentiel est infini!",
        "🎯 Focus sur l'essentiel!",
        "🌈 Petites victoires = Grand succès!",
        "🔥 Vous êtes plus fort que vous ne le pensez!"
    };
    
    private static final Random random = new Random();
    
    public static String getRandomMessage() {
        int index = random.nextInt(MESSAGES.length);
        return MESSAGES[index];
    }
    
    public static String getTaskCompletionMessage(String taskTitle) {
        String[] completionMessages = {
            "Bravo! ✅ " + taskTitle + " est terminée!",
            "Super travail! 🎉 Vous avez complété: " + taskTitle,
            "Félicitations! ✨ Tâche accomplie: " + taskTitle,
            "Une de moins! 💪 " + taskTitle + " est faite!",
            "Excellent! 🌟 " + taskTitle + " est maintenant terminée!"
        };
        return completionMessages[random.nextInt(completionMessages.length)];
    }
    
    public static String getMorningMotivation() {
        String[] morningMessages = {
            "☀️ Bonjour! Prêt pour une journée productive?",
            "🌅 Nouveau jour, nouvelles opportunités!",
            "🌞 Le meilleur moment pour agir, c'est maintenant!",
            "✨ Que votre journée soit aussi brillante que vous!",
            "🚀 Objectif du jour: accomplir au moins 3 tâches!"
        };
        return morningMessages[random.nextInt(morningMessages.length)];
    }
    
    public static String getEveningMotivation() {
        String[] eveningMessages = {
            "🌙 Bravo pour votre journée! Prenez du repos.",
            "⭐ Vous avez fait de votre mieux aujourd'hui!",
            "🌜 Demain est une nouvelle chance!",
            "✨ Reposez-vous, vous l'avez mérité!",
            "💫 Félicitations pour tout ce que vous avez accompli!"
        };
        return eveningMessages[random.nextInt(eveningMessages.length)];
    }
}
