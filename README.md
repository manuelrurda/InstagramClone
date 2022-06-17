# Project 3 - *Instagram Clone*

**Instagram** is a photo sharing app using Parse as its backend.

Time spent: **24** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User sees app icon in home screen.
- [x] User can sign up to create a new account using Parse authentication
- [x] User can log in to their account
- [x] The current signed in user is persisted across app restarts
- [x] User can log out of their account
- [x] User can take a photo, add a caption, and post it to "Instagram"
- [x] User can view the last 20 posts submitted to "Instagram"
- [x] User can pull to refresh the last 20 posts submitted to "Instagram"
- [x] User can see a post timestamp and caption.

The following **stretch** features are implemented:

- [x] Style the login page to look like the real Instagram login page.
- [x] Style the feed to look like the real Instagram feed.
- [ ] User can load more posts once they reach the bottom of the feed using endless scrolling.
- [x] User should switch between different tabs using fragments and a Bottom Navigation View.
    - [x] Feed Tab (to view all posts from all users)
    - [x] Capture Tab (to make a new post using the Camera and Photo Gallery)
    - [x] Profile Tab (to view only the current user's posts, in a grid)
- [x] Show the username and creation time for each post
- User Profiles:
    - [ ] Allow the logged in user to add a profile photo
    - [x] Display the profile photo with each post
    - [ ] Tapping on a post's username or profile photo goes to that user's profile page
    - [x] User Profile shows posts in a grid
- [ ] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse
- [ ] User can comment on a post and see all comments for each post in the post details screen.
- [x] User can like a post and see number of likes for each post.

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. I'd love to see detailed explanation of an efficient solution for the like management system.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/manuelrurda/InstagramClone/blob/main/instagram_showcase.gif' title='Video Walkthrough' width='300' alt='Video Walkthrough' />

GIF created with [Kap](https://getkap.co/).

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library
- [Parse Database Manager](https://www.back4app.com/) - parse database
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android


## Notes

Like management system was a real challenge. I decided to approach my solution as you would in a relational database. 
However, there is a lot of room for improvement, specially regarding optimization.

## License

    Copyright [2022] [Manuel Rodriguez Urdapilleta]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.