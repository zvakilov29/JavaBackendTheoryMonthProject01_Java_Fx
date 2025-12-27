package rpg.battle.actions;

import rpg.battle.BattleAction;
import rpg.battle.BattleContext;
import rpg.model.Player;
import rpg.ui.GameUI;

public class SpecialAction implements BattleAction {

    @Override
    public String label() {
        return "Special Skill";
    }

    @Override
    public void execute(BattleContext ctx) {
        Player p = ctx.getPlayer();
        GameUI ui = ctx.getUi();

        if (!p.canUseSkill()) {
            ui.println("Skill is on cooldown for " + p.getSkillCooldownRemaining() + " more turn(s).");
            return;
        }

        p.getSkill().use(ctx);
        p.startSkillCooldown();
    }
}
