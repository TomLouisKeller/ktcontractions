# ToDo

fix up this file and the data/expand/README.md

look at https://github.com/ian-beaver/pycontractions if they have something that we don't have

## Functionality

currently couldn’t’ve is split into could not've if only single_contractions.json is added - this should not be the case. it should stay the same.
unfortunately \b does match '’`. we have to find a way around that

\bcouldn['’`]t(^\w|\w$|\W\w|\w\W)

- run tests and fix documentation examples !!
- use testing framework to speed up tests (so we don't have to load everything every time from Contractions::default())
- Remove She's, He's, It's
- Use parts of speech tagging to figure out if the next word after (She's/...) is a verb, this way we can resolve it
- Use parts-of-speech tagging or named-entity-recognition to figure out if the 's is a possessive or a 'is'
