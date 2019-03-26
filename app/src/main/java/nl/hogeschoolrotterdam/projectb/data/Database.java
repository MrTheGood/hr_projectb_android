package nl.hogeschoolrotterdam.projectb.data;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
@SuppressWarnings("unused")
public class Database {
    private static Database ourInstance = new Database();
    private final ArrayList<Memory> memories = new ArrayList<>();

    private Database() {
        memories.add(new Memory("01", new LatLng(52.1326,5.2913), new Date(1546300800000L), "Happy new year!", "We had a lot of fun during the new year! We shot some people with fireworks, lost 21 fingers, ate some weird fried doughballs and killed Kenny.\nBasically just had a good time.", null));
        memories.add(new Memory("02", new LatLng(52.0226,5.2913), new Date(0L), "I'm sorry Dave", "I'm affraid I can't do that.", null));
        memories.add(new Memory("03", new LatLng(52.2326,5.2913), new Date(1546300700000L), "HI.", "It was a very fun night, hicham only now has a soa but he doesnt mind, he also.. nvm", null));
        memories.add(new Memory("04", new LatLng(52.3326,5.2913), new Date(1546300700000L), "HI.", "Hi how are you i am very ogoeood idk what is hould rwite so this iks josut some tpy ol riddled texct full of typos an dtedt and bullshit idk what else do youlw an tmee to say?! jusot give me some thing to say! stem partyt egen de bourger, sterven krijgkt de tegin gplease actuwola y dont i don't awoant that tho wn happeabnl now aphmake ikt stop", null));
        memories.add(new Memory("05", new LatLng(52.4326,5.2913), new Date(1524239892345L), "One Ring", "One ring to rule them all, one ring to find them. One ring to bring them all and in the darkness bind them.", null));
        memories.add(new Memory("06", new LatLng(52.5326,5.2913), new Date(1548423697889L), "KSLh iokjBS DLIOKbi KWBI olBS ifjkb wikahsbdf ikwba sidk", "sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw.", null));
        memories.add(new Memory("07", new LatLng(52.6326,5.2913), new Date(1524239892345L), "One Ring", "One ring to rule them all, one ring to find them. One ring to bring them all and in the darkness bind them.", null));
        memories.add(new Memory("08", new LatLng(52.8326,5.2913), new Date(0L), "I'm sorry Dave", "I'm affraid I can't do that.", null));
        memories.add(new Memory("09", new LatLng(52.7326,5.2913), new Date(1546300800000L), "Happy new year!", "We had a lot of fun during the new year! We shot some people with fireworks, lost 21 fingers, ate some weird fried doughballs and killed Kenny.\nBasically just had a good time.", null));
        memories.add(new Memory("10", new LatLng(52.9326,5.2913), new Date(1546323697889L), "Night Gathers.", "Night gathers and now my watch begins. It shall not end untill my death. I shall take no wives, hold no lands, father no children. I shall live and die at my post. I am the sword in the darkness. I am the watcher on the walls. I am the light in the darkness, the sword that brings the dawn, the horn that wakes the sleepers, the shield that guards the realms of men. I give my life and honor to the Nights Watch, for this night and all nights to come.", null));
        memories.add(new Memory("11", new LatLng(52.9926,5.2913), new Date(1548423697889L), "KSLh iokjBS DLIOKbi KWBI olBS ifjkb wikahsbdf ikwba sidk", "sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba ", null));
        memories.add(new Memory("12", new LatLng(51.0326,5.2913), new Date(1546323697889L), "Night Gathers.", "Night gathers and now my watch begins. It shall not end untill my death. I shall take no wives, hold no lands, father no children. I shall live and die at my post. I am the sword in the darkness. I am the watcher on the walls. I am the light in the darkness, the sword that brings the dawn, the horn that wakes the sleepers, the shield that guards the realms of men. I give my life and honor to the Nights Watch, for this night and all nights to come.", null));
        memories.add(new Memory("13", new LatLng(51.1326,5.2913), new Date(1546300800000L), "Happy new year!", "We had a lot of fun during the new year! We shot some people with fireworks, lost 21 fingers, ate some weird fried doughballs and killed Kenny.\nBasically just had a good time.", null));
        memories.add(new Memory("14", new LatLng(51.2326,5.2913), new Date(1546300800000L), "Happy new year!", "We had a lot of fun during the new year! We shot some people with fireworks, lost 21 fingers, ate some weird fried doughballs and killed Kenny.\nBasically just had a good time.", null));
        memories.add(new Memory("15", new LatLng(51.3326,5.2913), new Date(1546300700000L), "HI.", "Hi how are you i am very ogoeood idk what is hould rwite so this iks josut some tpy ol riddled texct full of typos an dtedt and bullshit idk what else do youlw an tmee to say?! jusot give me some thing to say! stem partyt egen de bourger, sterven krijgkt de tegin gplease actuwola y dont i don't awoant that tho wn happeabnl now aphmake ikt stop", null));
        memories.add(new Memory("16", new LatLng(51.4326,5.2913), new Date(0L), "I'm sorry Dave", "I'm affraid I can't do that.", null));
        memories.add(new Memory("17", new LatLng(51.5326,5.2913), new Date(1548423697889L), "KSLh iokjBS DLIOKbi KWBI olBS ifjkb wikahsbdf ikwba sidk", "sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw.", null));
        memories.add(new Memory("18", new LatLng(51.6326,5.2913), new Date(1524239892345L), "One Ring", "One ring to rule them all, one ring to find them. One ring to bring them all and in the darkness bind them.", null));
        memories.add(new Memory("19", new LatLng(51.7326,5.2913), new Date(1524239892345L), "One Ring", "One ring to rule them all, one ring to find them. One ring to bring them all and in the darkness bind them.", null));
        memories.add(new Memory("20", new LatLng(51.8326,5.2913), new Date(0L), "I'm sorry Dave", "I'm affraid I can't do that.", null));
        memories.add(new Memory("21", new LatLng(51.9326,5.2913), new Date(1546323697889L), "Night Gathers.", "Night gathers and now my watch begins. It shall not end untill my death. I shall take no wives, hold no lands, father no children. I shall live and die at my post. I am the sword in the darkness. I am the watcher on the walls. I am the light in the darkness, the sword that brings the dawn, the horn that wakes the sleepers, the shield that guards the realms of men. I give my life and honor to the Nights Watch, for this night and all nights to come.", null));
        memories.add(new Memory("22", new LatLng(50.9326,5.2913), new Date(1548423697889L), "KSLh iokjBS DLIOKbi KWBI olBS ifjkb wikahsbdf ikwba sidk", "sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb.", null));
        memories.add(new Memory("23", new LatLng(50.8326,5.2913), new Date(1546323697889L), "Night Gathers.", "Night gathers and now my watch begins. It shall not end untill my death. I shall take no wives, hold no lands, father no children. I shall live and die at my post. I am the sword in the darkness. I am the watcher on the walls. I am the light in the darkness, the sword that brings the dawn, the horn that wakes the sleepers, the shield that guards the realms of men. I give my life and honor to the Nights Watch, for this night and all nights to come.", null));
        memories.add(new Memory("24", new LatLng(50.7326,5.2913), new Date(1546300700000L), "HI.", "Hi how are you i am very ogoeood idk what is hould rwite so this iks josut some tpy ol riddled texct full of typos an dtedt and bullshit idk what else do youlw an tmee to say?! jusot give me some thing to say! stem partyt egen de bourger, sterven krijgkt de tegin gplease actuwola y dont i don't awoant that tho wn happeabnl now aphmake ikt stop", null));
    }

    @NonNull
    public static Database getInstance() {
        return ourInstance;
    }


    @NonNull
    public List<Memory> getMemories() {
        return memories;
    }

    public void addMemory(Memory memory) {
        memories.add(memory);
    }

    /**
     * Use this to find a single memory, for instance when opening the detail page.
     *
     * @param memoryId memory to find
     * @return the memory with id memoryId, or null if none found.
     */
    @Nullable
    public Memory findMemory(String memoryId) {
        for (Memory memory : memories) {
            if (memory.getId().equals(memoryId))
                return memory;
        }
        return null;
    }
}
