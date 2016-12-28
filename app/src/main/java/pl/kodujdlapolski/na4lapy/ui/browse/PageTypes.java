/*
 *	Copyright 2017 Stowarzyszenie Na4Łapy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package pl.kodujdlapolski.na4lapy.ui.browse;

import pl.kodujdlapolski.na4lapy.R;
import pl.kodujdlapolski.na4lapy.model.type.Species;

public enum PageTypes {
    ALL(R.string.list_section_all, null),
    DOGS(Species.DOG.getLabelResId(), Species.DOG),
    CATS(Species.CAT.getLabelResId(), Species.CAT),
    OTHER(Species.OTHER.getLabelResId(), Species.OTHER);
    public int nameResId;
    Species specie;

    PageTypes(int nameResId, Species specie) {
        this.nameResId = nameResId;
        this.specie = specie;
    }
}
