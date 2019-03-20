package nl.hogeschoolrotterdam.projectb.data;

import android.location.Location;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
@SuppressWarnings("unused")
public class Database {
    private static Database ourInstance = new Database();
    private final ArrayList<Memory> memories = new ArrayList<>();

    private Database() {
        memories.add(new Memory("0", new Location("demo"), new Date(1524239892345L), "One Ring", "One ring to rule them all, one ring to find them. One ring to bring them all and in the darkness bind them.", null));
        memories.add(new Memory("1", new Location("demo"), new Date(0L), "I'm sorry Dave", "I'm affraid I can't do that.", null));
        memories.add(new Memory("2", new Location("demo"), new Date(1546300800000L), "Happy new year!", "We had a lot of fun during the new year! We shot some people with fireworks, lost 21 fingers, ate some weird fried doughballs and killed Kenny.\nBasically just had a good time.", null));
        memories.add(new Memory("3", new Location("demo"), new Date(1546300700000L), "HI.", "Hi how are you i am very ogoeood idk what is hould rwite so this iks josut some tpy ol riddled texct full of typos an dtedt and bullshit idk what else do youlw an tmee to say?! jusot give me some thing to say! stem partyt egen de bourger, sterven krijgkt de tegin gplease actuwola y dont i don't awoant that tho wn happeabnl now aphmake ikt stop", null));
        memories.add(new Memory("4", new Location("demo"), new Date(1546323697889L), "Night Gathers.", "Night gathers and now my watch begins. It shall not end untill my death. I shall take no wives, hold no lands, father no children. I shall live and die at my post. I am the sword in the darkness. I am the watcher on the walls. I am the light in the darkness, the sword that brings the dawn, the horn that wakes the sleepers, the shield that guards the realms of men. I give my life and honor to the Nights Watch, for this night and all nights to come.", null));
        memories.add(new Memory("5", new Location("demo"), new Date(1548423697889L), "KSLh iokjBS DLIOKbi KWBI olBS ifjkb wikahsbdf ikwba sidk", "sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.sJDKh wiokjnabsd oflhnb oiklasdnbf oupiwklhba sdifklhb ilkasdjbiuw jkbadiulkjhb i lakewjbsd ifhkjnbwaiglduhkfj beiajhksd b.", null));
    }

    @NonNull
    public static Database getInstance() {
        return ourInstance;
    }


    @NonNull
    public ArrayList<Memory> getMemories() {
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
