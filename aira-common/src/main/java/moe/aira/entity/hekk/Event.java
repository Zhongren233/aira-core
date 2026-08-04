package moe.aira.entity.hekk;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Event {

    public Integer id;
    public String name;
    public String description;
    public Integer chapter_id;
    public Date began_at;
    public Date ended_at;
    public Date counting_ended_at;
    public Date display_ended_at;
    public List<Integer> music_ids;
    public List<Integer> talk_event_ids;
    public Integer pass_item_id;
    public String event_bonus_card_description;
    public String event_bonus_skill_description;
    public Date created_at;
    public Date updated_at;
    public boolean enabled;
    public Date receive_ended_at;
    public Integer rule_asset_count;
    //    public EventStatusBonusRateMap event_status_bonus_rate_map;
    public List<String> bgm_asset_codes;
    public Integer live_event_pt;
    public Integer work_event_pt;
    public Integer work_id;
    public String type;
    public String bgm_asset_code;
//    public CardIdEventPtBonusRateMap card_id_event_pt_bonus_rate_map;
}
