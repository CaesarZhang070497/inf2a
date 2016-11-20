# File: pos_tagging.py
# Template file for Informatics 2A Assignment 2:
# 'A Natural Language Query System in Python/NLTK'

# John Longley, November 2012
# Revised November 2013 and November 2014 with help from Nikolay Bogoychev
# Revised November 2015 by Toms Bergmanis and Shay Cohen
# Revised November 2016 by Adam Lopez

# PART B: POS tagging

from statements import *

# The tagset we shall use is:
# P  A  Ns  Np  Is  Ip  Ts  Tp  BEs  BEp  DOs  DOp  AR  AND  WHO  WHICH  ?

# Tags for words playing a special role in the grammar:

function_words_tags = [('a', 'AR'), ('an', 'AR'), ('and', 'AND'),
					   ('is', 'BEs'), ('are', 'BEp'), ('does', 'DOs'), ('do', 'DOp'),
					   ('who', 'WHO'), ('which', 'WHICH'), ('Who', 'WHO'), ('Which', 'WHICH'), ('?', '?')]
# upper or lowercase tolerated at start of question.

function_words = [p[0] for p in function_words_tags]


def unchanging_plurals():
	NN = set()
	NNS = set()
	with open("sentences.txt", "r") as f:
		for line in f:
			for word, pos in map(lambda x: x.split('|'), line.split(' ')):
				if pos == 'NN':
					NN.add(word)
				elif pos == 'NNS':
					NNS.add(word)
	return list(NN.intersection(NNS))


unchanging_plurals_list = unchanging_plurals()
brown_taggedset = set(brown.tagged_words())

def noun_stem(s):
	def match(pattern):
		return re.match(pattern + '$', s, re.IGNORECASE)

	if s in unchanging_plurals_list:
		return s
	if (s, 'NNS') not in brown_taggedset:
		return ''
	if match('.*ves'):
		if (s[:-3] + 'f', 'NN') in brown_taggedset:
			return s[:-3] + 'f'
		if (s[:-3] + 'fe', 'NN') in brown_taggedset:
			return s[:-3] + 'fe'
	if match('.*(?<!.[sxyzaeiou]|ch|sh)s'):
		return s[:-1]
	if match('.*[aeiou]ys'):
		return s[:-1]
	if match('.*.[^aeiou]ies'):
		return s[:-3] + 'y'
	if match('[^aeiou]ies'):
		return s[:-1]
	if match('.*([ox]|ch|sh|ss|zz)es'):
		return s[:-2]
	if match('.*(o|x|ch|sh|ss|zz)es'):
		return s[:-2]
	if match('.*([^s]se|[^z]ze)s'):
		return s[:-1]
	if match('.*(?<!.[iosxz]|ch|sh)es'):
		return s[:-1]
	return ''


def tag_word(lx, wd):
	tagset = {tag for (word, tag) in function_words_tags if word == wd}
	fixed_lxtags = ['P', 'A']				# Tags with only 1 form
	variable_noun_lxtags = ['N']			# Noun tags with singular/plural forms
	variable_verb_lxtags = ['I', 'T']		# Verb tags with singular/plural forms

	for tag in fixed_lxtags:
		if wd in lx.getAll(tag):
			tagset.add(tag)
	for tag in variable_noun_lxtags:
		tagged_words = lx.getAll(tag)
		if wd in tagged_words:
			tagset.add(tag + 's')
		if noun_stem(wd) in tagged_words:
			tagset.add(tag + 'p')
	for tag in variable_verb_lxtags:
		tagged_words = lx.getAll(tag)
		if wd in tagged_words:
			tagset.add(tag + 'p')
		if verb_stem(wd) in tagged_words:
			tagset.add(tag + 's')
	return list(tagset)


def tag_words(lx, wds):
	"""returns a list of all possible taggings for a list of words"""
	if (wds == []):
		return [[]]
	else:
		tag_first = tag_word(lx, wds[0])
		tag_rest = tag_words(lx, wds[1:])
		return [[fst] + rst for fst in tag_first for rst in tag_rest]

# End of PART B.
