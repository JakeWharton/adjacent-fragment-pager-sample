Adjacent Fragment Pager Sample
==============================

Demonstrates how to manage two fragments where portrait displays them in a `ViewPager` and landscape
displays them side-by-side.

Due the shenanigans performed by `FragmentPagerAdapter` we're forced to write a custom
`PagerAdapter` which handles the instances our selves.

This sample is very-much hard coded and specific to two pages. If you wanted something a bit more
robust and generalized it wouldn't be too much work to do so.


*Note: Requires r12 of the support-v4 library that is not available in Maven central.*




License
-------

    Copyright 2013 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.