README
======

This project contains a plugin for the [Locale application](http://www.twofortyfouram.com/) which allows
you to change your phone settings depending on whether one of your calendars has an entry or not. I find it quite useful
to make sure that my phone is set to silent mode whenever the calendar I use for work related appointments
contains an entry.

Compatibility
-------------

So far the application was only tested on Android 1.5. Since the emulator does not contain the Google apps and
the calendar provider is therefore unavailable testing on different versions of Android is rather difficult.

Features
--------
A single calendar situation can check for the following conditions:
- a calendar contains an appointment
- the appointment's title does not contain one of a list of words
- the appointment has already started or starts in 5 or 10 minutes
