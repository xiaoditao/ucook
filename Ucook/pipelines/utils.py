from django.db import models
from django.contrib.auth.models import User
from ..models import Profile as ProfileModel


def create_profile(strategy, details, response, user, *args, **kwargs):
    username = details['username']
    user_object = User.objects.get(username=username)
    if ProfileModel.objects.filter(user=user_object).exists():
        pass
    else:
        new_profile = ProfileModel(user=user_object)
        new_profile.save()
    return kwargs
