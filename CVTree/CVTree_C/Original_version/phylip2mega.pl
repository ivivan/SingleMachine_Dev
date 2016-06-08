#!/usr/bin/env perl
use strict;
if(@ARGV<2)
{
	print "$0 PHYLIP.matrix MEGA.matrix\n";
	exit(1);
}
my $old_file=$ARGV[0];
my $new_file=$ARGV[1];

my @mlist;
my %matrix;
open(F,"$old_file")||die "open $old_file error\n";
<F>;
my @mlist;
while(<F>)
{
	chomp $_;
	my @tmp =split(" ",$_);
	for(my $i=0;$i<@mlist;++$i)
	{
		$matrix{$mlist[$i]}{$tmp[0]}=$tmp[$i+1];
		$matrix{$tmp[0]}{$mlist[$i]}=$tmp[$i+1];
	}
	push @mlist,$tmp[0];
}
close F;


open (OUT,">$new_file")||die "open $new_file error\n";
print OUT "#mega\n";
print OUT "Title: Lower-left triangular matrix\n\n";
foreach(@mlist)
{
	print OUT "#$_\n";
}
print OUT "\n";

for(my $i=1;$i<@mlist;++$i)
{
	for(my $j=0;$j<$i;++$j)
	{
		printf OUT ("%.10f ",$matrix{$mlist[$i]}{$mlist[$j]});
	}
	print OUT "\n";
}
close OUT;
