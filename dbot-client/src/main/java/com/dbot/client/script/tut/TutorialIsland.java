package com.dbot.client.script.tut;

import com.google.inject.Inject;
import net.runelite.api.GameState;
import net.runelite.api.VarPlayer;
import net.runelite.api.script.PollingScript;

public class TutorialIsland extends PollingScript {

    public TutorialIsland() {
        super(0);
    }

    @Override
    public void poll() {
        if (client.getGameState() != GameState.LOGGED_IN)
            return;

        if (client.getVar(VarPlayer.TUTORIAL_ISLAND_PROGRESS) == 1000) {
            return;
        }

        int section = getSection();

        if (section >= 0 && section <= 1) {
            guideSection.poll();
        } else if (section >= 2 && section <= 3) {
            survivalSection.poll();
        } else if (section >= 4 && section <= 5) {
            cookSection.poll();
        } else if (section >= 6 && section <= 7) {
            questSection.poll();
        } else if (section >= 8 && section <= 9) {
            miningSection.poll();
        } else if (section >= 10 && section <= 12) {
            combatSection.poll();
        } else if (section >= 14 && section <= 15) {
            bankSection.poll();
        } else if (section >= 16 && section <= 17) {
            priestSection.poll();
        } else if (section >= 18 && section <= 20) {
            wizardSection.poll();
        }
    }

    private int getSection() {
        return client.getVar(VarPlayer.TUTORIAL_ISLAND_SECTION);
    }

    @Inject
    private GuideSection guideSection;

    @Inject
    private SurvivalSection survivalSection;

    @Inject
    private CookSection cookSection;

    @Inject
    private QuestSection questSection;

    @Inject
    private MiningSection miningSection;

    @Inject
    private CombatSection combatSection;

    @Inject
    private BankSection bankSection;

    @Inject
    private PriestSection priestSection;

    @Inject
    private WizardSection wizardSection;

}
