package com.riyandifirman.storyapp.utils

import com.riyandifirman.storyapp.response.ListStoryItem

object DataDummy {

    fun generateDummyAllStory(): List<ListStoryItem> {
        val listStory = ArrayList<ListStoryItem>()
        for (i in 0..30) {
            val story = ListStoryItem(
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-02-22T22:22:22Z",
                "brimstone $i",
                "Valorant GO! $i",
                "107.6338462",
                "$i",
                "-6.8957643",
            )
            listStory.add(story)
        }
        return listStory
    }

}