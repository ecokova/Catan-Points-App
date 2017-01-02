# Catan-Points-App
The Cities and Knights expansion of Settlers of Catan is fantastic, but keeping track of victory points is ridiculously complicated. This app will hopefully take the place of the human Points Wench and help us not need to recount points every 10 minutes.

### TODO List
**Support for Additional Expansions**
- Research points and starting positions for all of the different expansions
- Add support for longest trade route, largest army, etc in Player UI
- Add "custom/multiple" expansions option, where the user can pick which actions they want to use (will need to pass that to GameActivity)

**Long-term Goals**
- Voice control using API.ai. Hope to be able to say "Hey Catan, Monica stole Catherine's metropolis" and have it update the points accordingly

**Bugs/Needswork**
- Need an option to delete/undo an action!! eg: Someone loses longest road because their 5th road was removed; they no longer have longest road, but neither does anyone else. Also misclicks or miscommunication might happen.
- Card victory point graphic should be different - the base game has that too, but they're dev cards, not progress cards. Need to pick something consistent across all expansions.

**Code Cleanup/Improvements**
- Need to centralize the expansion-specific details (eg: what actions are available, what things you can get victory points from, what the starting conditions are, etc), and possibly use that to map each entity to its respective player method, resource ID, view ID, etc
- Should have a single Player method that takes an entity (eg: "Constants.BLUE_METROPOLIS") and returns whether the player has that. This would be better than having to call hasMerchant(), getMetropolises().contains(), hasLongestRoad(), etc for things where you perform the same operation for each thing (eg: making an icon visible).
